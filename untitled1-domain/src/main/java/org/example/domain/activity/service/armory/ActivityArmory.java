package org.example.domain.activity.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.entity.ActivitySkuStockVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ActivityArmory implements IActivityArmory, IActivityDispatch {

    @Autowired
    IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        //1 查询sku
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        //2 写redis
        cacheActivityStockSku(sku, activitySkuEntity.getStockCount());
        //3 查询 装配
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        return false;

    }

    public void cacheActivityStockSku(Long sku, Integer stockCount) {
        String cacheKEy = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivityStockSku(cacheKEy, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuCount(Long sku, Date endDateTime) {
        String cacheKEy = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.substractActivitySkuCount(sku, cacheKEy, endDateTime);
    }

}
