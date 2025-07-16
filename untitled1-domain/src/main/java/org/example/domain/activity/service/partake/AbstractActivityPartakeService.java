package org.example.domain.activity.service.partake;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.model.vo.ActivityStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.IRaffleActivityPartakeService;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

import java.util.Date;

@Slf4j
public abstract class AbstractActivityPartakeService implements IRaffleActivityPartakeService {
    IActivityRepository activityRepository;

    AbstractActivityPartakeService(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        //0.先拿到基础信息
        String userId = partakeRaffleActivityEntity.getUserId();
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        Date currentDate = new Date();

        //1.活动查询
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        if (!activityEntity.getState().equals(ActivityStateVO.open)){
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        if (!activityEntity.getBeginDateTime().before(currentDate) || !activityEntity.getEndDateTime().after(currentDate)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }

        //2.查询没有被活动使用过的订单信息
        UserRaffleOrderEntity raffleOrderEntity  = activityRepository.queryNoUsedRaffleOrder(partakeRaffleActivityEntity);
        if (null != raffleOrderEntity) {
            log.info("已存在，未消费的订单: {}", raffleOrderEntity);
            return raffleOrderEntity;
        }

        //3.账户额度过滤
        CreatePartakeOrderAggregate createPartakeOrderAggregate = doFilterAccount(userId, activityId, currentDate);


        //4.创建订单
        UserRaffleOrderEntity userRaffleOrderEntity = createUserRaffleOrder(userId, activityId, currentDate);

        //5.聚合
        createPartakeOrderAggregate.setUserRaffleOrder(userRaffleOrderEntity);

        //6.保存入库
        activityRepository.savePartakeOrderAggregate(createPartakeOrderAggregate);

        return userRaffleOrderEntity;

    }

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);
    protected abstract UserRaffleOrderEntity createUserRaffleOrder(String userId, Long activityId, Date currentDate);
}
