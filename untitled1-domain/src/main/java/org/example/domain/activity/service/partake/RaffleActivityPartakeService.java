package org.example.domain.activity.service.partake;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.vo.UserRaffleStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RaffleActivityPartakeService extends AbstractActivityPartakeService {
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
    RaffleActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccount(userId, activityId);
        if (null == activityAccountEntity || activityAccountEntity.getTotalCount() == 0){
            throw new AppException(ResponseCode.ACTIVITY_ACCOUNT_ERROR.getCode(), ResponseCode.ACTIVITY_ACCOUNT_ERROR.getInfo());
        }

        String month = monthFormat.format(currentDate);
        ActivityMonthCountEntity activityMonthCountEntity = activityRepository.queryActivityMonthAccount(userId, activityId, month);
        if (null != activityMonthCountEntity && activityMonthCountEntity.getMonthCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACTIVITY_MONTH_ACCOUNT_ERROR.getCode(), ResponseCode.ACTIVITY_MONTH_ACCOUNT_ERROR.getInfo());
        }
        boolean isExistMonth = null != activityMonthCountEntity;
        if (!isExistMonth){
            ActivityMonthCountEntity monthCountEntity = new ActivityMonthCountEntity();
            monthCountEntity.setMonth(month);
            monthCountEntity.setActivityId(activityId);
            monthCountEntity.setUserId(userId);
            monthCountEntity.setMonthCount(activityAccountEntity.getMonthCount());
            monthCountEntity.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
            activityMonthCountEntity = monthCountEntity;
        }

        String day = dayFormat.format(currentDate);
        ActivityDayCountEntity activityDayCountEntity = activityRepository.queryActivityDayAccount(userId, activityId, day);
        if (null != activityDayCountEntity && activityDayCountEntity.getDayCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACTIVITY_DAY_ACCOUNT_ERROR.getCode(), ResponseCode.ACTIVITY_DAY_ACCOUNT_ERROR.getInfo());
        }
        boolean isExistDay = null != activityDayCountEntity;
        if (!isExistDay){
            ActivityDayCountEntity dayCountEntity = new ActivityDayCountEntity();
            dayCountEntity.setActivityId(activityId);
            dayCountEntity.setUserId(userId);
            dayCountEntity.setDay(day);
            dayCountEntity.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
            dayCountEntity.setDayCount(activityAccountEntity.getDayCount());
            activityDayCountEntity = dayCountEntity;
        }

        CreatePartakeOrderAggregate createPartakeOrderAggregate = new CreatePartakeOrderAggregate();
        createPartakeOrderAggregate.setUserId(userId);
        createPartakeOrderAggregate.setActivityId(activityId);
        createPartakeOrderAggregate.setActivityAccount(activityAccountEntity);
        createPartakeOrderAggregate.setActivityDayCount(activityDayCountEntity);
        createPartakeOrderAggregate.setActivityMonthCount(activityMonthCountEntity);
        createPartakeOrderAggregate.setExistDayCount(isExistDay);
        createPartakeOrderAggregate.setExistMonthCount(isExistMonth);
        return createPartakeOrderAggregate;
    }

    @Override
    protected UserRaffleOrderEntity createUserRaffleOrder(String userId, Long activityId, Date currentDate) {
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        // 构建订单
        UserRaffleOrderEntity userRaffleOrder = new UserRaffleOrderEntity();
        userRaffleOrder.setUserId(userId);
        userRaffleOrder.setActivityId(activityId);
        userRaffleOrder.setActivityName(activityEntity.getActivityName());
        userRaffleOrder.setStrategyId(activityEntity.getStrategyId());
        userRaffleOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        userRaffleOrder.setOrderTime(currentDate);
        userRaffleOrder.setOrderState(UserRaffleStateVO.create);
        return userRaffleOrder;
    }
}
