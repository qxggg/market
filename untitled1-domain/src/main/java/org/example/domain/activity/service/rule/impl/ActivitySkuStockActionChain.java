package org.example.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.entity.ActivitySkuStockVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.armory.IActivityDispatch;
import org.example.domain.activity.service.rule.AbstractActionChain;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 商品库存规则节点
 * @create 2024-03-23 10:25
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    IActivityDispatch activityDispatch;

    @Resource
    IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        boolean status = activityDispatch.subtractionActivitySkuCount(activitySkuEntity.getSku(), activityEntity.getEndDateTime());
        if (status) {
            log.info("活动责任链-商品库存处理【校验&扣减】成功。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
            activityRepository.activitySkuCountSendConsume(ActivitySkuStockVO.builder()
                    .activityId(activityEntity.getActivityId())
                    .sku(activitySkuEntity.getSku())
                    .build());
            return true;
        }
        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
    }

}
