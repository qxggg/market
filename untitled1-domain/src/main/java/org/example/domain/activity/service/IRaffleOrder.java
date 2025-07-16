package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.*;

public interface IRaffleOrder {
    ActivityOrderEntity createOrder(ActivityShopCartEntity activityShopCartEntity);

    String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity);
}
