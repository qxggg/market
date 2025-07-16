package org.example.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.event.ActivitySkuStockSendZeroMessage;
import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.vo.ActivitySkuStockVO;
import org.example.domain.activity.model.vo.ActivityStateVO;
import org.example.domain.activity.model.vo.UserRaffleStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.infrastructure.event.EventPublisher;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.po.*;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    protected IRaffleActivitySkuDao activitySkuDao;
    @Resource
    protected IRaffleActivityOrderDao activityOrderDao;
    @Resource
    protected IRaffleActivityAccountDao activityAccountDao;
    @Resource
    protected IRaffleActivityDao activityDao;
    @Resource
    protected IRedisService redisService;
    @Resource
    protected IRaffleActivityCountDao activityCountDao;
    @Resource
    protected IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    EventPublisher eventPublisher;
    @Resource
    ActivitySkuStockSendZeroMessage activitySkuStockSendZeroMessage;
    @Resource
    IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    IRaffleActivityAccountMonthDao raffleActivityAccountMonthdao;
    @Resource
    IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
    @Resource
    IUserRaffleOrderDao userRaffleOrderDao;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = activitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) return activityEntity;
        // 从库中获取数据
        RaffleActivity raffleActivity = activityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (null != activityCountEntity) return activityCountEntity;
        // 从库中获取数据
        RaffleActivityCount raffleActivityCount = activityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        //订单对象
        ActivityOrderEntity orderEntity = createOrderAggregate.getActivityOrderEntity();
        RaffleActivityOrder order = new RaffleActivityOrder();
        order.setUserId(orderEntity.getUserId());
        order.setSku(orderEntity.getSku());
        order.setActivityId(orderEntity.getActivityId());
        order.setActivityName(orderEntity.getActivityName());
        order.setStrategyId(orderEntity.getStrategyId());
        order.setOrderId(orderEntity.getOrderId());
        order.setOrderTime(orderEntity.getOrderTime());
        order.setTotalCount(orderEntity.getTotalCount());
        order.setDayCount(orderEntity.getDayCount());
        order.setMonthCount(orderEntity.getMonthCount());
        order.setTotalCount(createOrderAggregate.getTotalCount());
        order.setDayCount(createOrderAggregate.getDayCount());
        order.setMonthCount(createOrderAggregate.getMonthCount());
        order.setState(orderEntity.getState().getCode());
        order.setOutBusinessNo(orderEntity.getOutBusinessNo());



        //账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());

        dbRouter.doRouter(createOrderAggregate.getUserId());
// 编程式事务
        transactionTemplate.execute(status -> {
            try {
                // 1. 写入订单
                activityOrderDao.insert(order);
                // 2. 更新账户
                int count = activityAccountDao.updateAccountQuota(raffleActivityAccount);
                // 3. 创建账户 - 更新为0，则账户不存在，创新新账户。
                if (0 == count) {
                    activityAccountDao.insert(raffleActivityAccount);
                }
                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", orderEntity.getUserId(), orderEntity.getActivityId(), orderEntity.getSku(), e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode());
            }
        });


    }

    @Override
    public void cacheActivityStockSku(String cacheKey, Integer stockCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, stockCount);
    }

    @Override
    public boolean substractActivitySkuCount(Long sku, String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus == 0){
            eventPublisher.publish(activitySkuStockSendZeroMessage.topic(), activitySkuStockSendZeroMessage.buildEventMessage(sku));
            return false;
        }else if (surplus < 0){
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        long expireMills = endDateTime.getTime() - System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMills, TimeUnit.MICROSECONDS);
        if (!lock){
            log.info("活动sku库存加锁失败 {}", lockKey);
            return false;
        }
        return true;
    }

    @Override
    public void activitySkuCountSendConsume(ActivitySkuStockVO activitySkuStockKeyVO) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<ActivitySkuStockVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);

    }

    @Override
    public ActivitySkuStockVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        destinationQueue.clear();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        raffleActivitySkuDao.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        raffleActivitySkuDao.clearActivitySkuStock(sku);
    }

    @Override
    public ActivityAccountEntity queryActivityAccount(String userId, Long activityId){
        RaffleActivityAccount account = new RaffleActivityAccount();
        account.setUserId(userId);
        account.setActivityId(activityId);
        RaffleActivityAccount res = activityAccountDao.queryActivityAccount(account);
        if (res == null) return null;
        return ActivityAccountEntity.builder()
                .activityId(res.getActivityId())
                .totalCount(res.getTotalCount())
                .userId(res.getUserId())
                .dayCountSurplus(res.getDayCountSurplus())
                .dayCount(res.getDayCount())
                .monthCount(res.getMonthCount())
                .monthCountSurplus(res.getMonthCountSurplus())
                .totalCountSurplus(res.getTotalCountSurplus())
                .build();
    }

    @Override
    public ActivityMonthCountEntity queryActivityMonthAccount(String userId, Long activityId, String month) {
        RaffleActivityAccountMonth raffleActivityAccountMonthReq = new RaffleActivityAccountMonth();
        raffleActivityAccountMonthReq.setUserId(userId);
        raffleActivityAccountMonthReq.setActivityId(activityId);
        raffleActivityAccountMonthReq.setMonth(month);
        RaffleActivityAccountMonth raffleActivityAccountMonthRes = raffleActivityAccountMonthdao.queryActivityAccountMonthByUserId(raffleActivityAccountMonthReq);
        if (null == raffleActivityAccountMonthRes) return null;
        // 2. 转换对象
        return ActivityMonthCountEntity.builder()
                .userId(raffleActivityAccountMonthRes.getUserId())
                .activityId(raffleActivityAccountMonthRes.getActivityId())
                .month(raffleActivityAccountMonthRes.getMonth())
                .monthCount(raffleActivityAccountMonthRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountMonthRes.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityDayCountEntity queryActivityDayAccount(String userId, Long activityId, String day) {
        // 1. 查询账户
        RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(day);
        RaffleActivityAccountDay raffleActivityAccountDayRes = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
        if (null == raffleActivityAccountDayRes) return null;
        // 2. 转换对象
        return ActivityDayCountEntity.builder()
                .userId(raffleActivityAccountDayRes.getUserId())
                .activityId(raffleActivityAccountDayRes.getActivityId())
                .day(raffleActivityAccountDayRes.getDay())
                .dayCount(raffleActivityAccountDayRes.getDayCount())
                .dayCountSurplus(raffleActivityAccountDayRes.getDayCountSurplus())
                .build();

    }

    @Override
    public void savePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
        String userId = createPartakeOrderAggregate.getUserId();
        Long activityId = createPartakeOrderAggregate.getActivityId();
        ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccount();
        ActivityMonthCountEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityMonthCount();
        ActivityDayCountEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityDayCount();
        UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrder();
        dbRouter.doRouter(userId);
        System.out.println(activityAccountEntity);
        transactionTemplate.execute(status -> {
            try{
                //1.总订单更新
                int totalCount = raffleActivityAccountDao.updateActivityAccountSubtractionQuota(
                        RaffleActivityAccount.builder()
                                .activityId(activityId)
                                .userId(userId)
                                .build()
                );
                if (1 != totalCount) {
                    status.setRollbackOnly();
                    log.warn("写入创建参与活动记录，更新总账户额度不足，异常 userId: {} activityId: {}", userId, activityId);
                    throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
                }
                //2.月更新
                if (createPartakeOrderAggregate.isExistMonthCount()) {
                    int monthCount = raffleActivityAccountMonthdao.updateActivityAccountMonthSubtractionQuota(
                            RaffleActivityAccountMonth.builder()
                                    .activityId(activityId)
                                    .userId(userId)
                                    .month(activityAccountMonthEntity.getMonth())
                                    .build()
                    );
                    if (1 != monthCount) {
                        status.setRollbackOnly();
                        log.warn("创建参与活动记录，更新月账户额度不足，异常 userId: {} activityId: {}", userId, activityId);
                        throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
                    }
                }
                else{
                    raffleActivityAccountMonthdao.insertActivityAccountMonth(RaffleActivityAccountMonth.builder()
                            .userId(activityAccountMonthEntity.getUserId())
                            .activityId(activityAccountMonthEntity.getActivityId())
                            .month(activityAccountMonthEntity.getMonth())
                            .monthCount(activityAccountMonthEntity.getMonthCount())
                            .monthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus() - 1)
                            .build());
                }

                if (createPartakeOrderAggregate.isExistDayCount()) {
                    int dayCount = raffleActivityAccountDayDao.updateActivityAccountDaySubtractionQuota(
                            RaffleActivityAccountDay.builder()
                                    .activityId(activityId)
                                    .userId(userId)
                                    .day(activityAccountDayEntity.getDay())
                                    .build()
                    );
                    if (1 != dayCount) {
                        status.setRollbackOnly();
                        log.warn("创建参与活动记录，更新日账户额度不足，异常 userId: {} activityId: {}", userId, activityId);
                        throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
                    }
                }
                //3.日更新
                else{
                    raffleActivityAccountDayDao.insertActivityAccountDay(
                            RaffleActivityAccountDay.builder()
                                    .userId(activityAccountDayEntity.getUserId())
                                    .activityId(activityAccountDayEntity.getActivityId())
                                    .day(activityAccountDayEntity.getDay())
                                    .dayCount(activityAccountDayEntity.getDayCount())
                                    .dayCountSurplus(activityAccountDayEntity.getDayCountSurplus() - 1)
                                    .build()
                    );
                }
                //4.插入订单
                userRaffleOrderDao.insert(UserRaffleOrder.builder()
                        .userId(userRaffleOrderEntity.getUserId())
                        .activityId(userRaffleOrderEntity.getActivityId())
                        .activityName(userRaffleOrderEntity.getActivityName())
                        .strategyId(userRaffleOrderEntity.getStrategyId())
                        .orderId(userRaffleOrderEntity.getOrderId())
                        .orderTime(userRaffleOrderEntity.getOrderTime())
                        .orderState(userRaffleOrderEntity.getOrderState().getCode())
                        .build());

                return 1;
            }catch (DuplicateKeyException e){
                status.setRollbackOnly();
                log.error("写入创建参与活动记录，唯一索引冲突 userId: {} activityId: {}", userId, activityId, e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
            }
            finally {
                dbRouter.clear();
            }
        });

    }

    @Override
    public UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        // 查询数据
        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(partakeRaffleActivityEntity.getUserId());
        userRaffleOrderReq.setActivityId(partakeRaffleActivityEntity.getActivityId());
        UserRaffleOrder userRaffleOrderRes = userRaffleOrderDao.queryNoUsedRaffleOrder(userRaffleOrderReq);
        if (null == userRaffleOrderRes) return null;
        // 封装结果
        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userRaffleOrderRes.getUserId());
        userRaffleOrderEntity.setActivityId(userRaffleOrderRes.getActivityId());
        userRaffleOrderEntity.setActivityName(userRaffleOrderRes.getActivityName());
        userRaffleOrderEntity.setStrategyId(userRaffleOrderRes.getStrategyId());
        userRaffleOrderEntity.setOrderId(userRaffleOrderRes.getOrderId());
        userRaffleOrderEntity.setOrderTime(userRaffleOrderRes.getOrderTime());
        userRaffleOrderEntity.setOrderState(UserRaffleStateVO.valueOf(userRaffleOrderRes.getOrderState()));
        return userRaffleOrderEntity;
    }


}
