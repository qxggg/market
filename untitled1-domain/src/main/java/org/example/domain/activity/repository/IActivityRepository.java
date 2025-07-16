package org.example.domain.activity.repository;

import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.vo.ActivitySkuStockVO;

import java.util.Date;

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    void cacheActivityStockSku(String cacheKEy, Integer stockCount);

    boolean substractActivitySkuCount(Long sku, String cacheKey, Date endDateTime);

    void activitySkuCountSendConsume(ActivitySkuStockVO activitySkuStockKeyVO);

    ActivitySkuStockVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    ActivityAccountEntity queryActivityAccount(String userId, Long activityId);

    ActivityMonthCountEntity queryActivityMonthAccount(String userId, Long activityId, String month);

    ActivityDayCountEntity queryActivityDayAccount(String userId, Long activityId, String day);

    void savePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
