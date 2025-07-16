package org.example.test.domain.service.activity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;
import org.example.domain.activity.model.entity.SkuRechargeEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.AbstractRaffleActivity;
import org.example.domain.activity.service.RaffleActivityService;
import org.example.domain.activity.service.armory.IActivityArmory;
import org.example.domain.activity.service.armory.IActivityDispatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class TestActivityService {
    @Autowired
    RaffleActivityService raffleActivity;
    @Autowired
    IActivityArmory activityArmory;
    @Test
    public void test() {
        raffleActivity.createOrder(ActivityShopCartEntity.builder()
                        .sku(9011L)
                        .userId("user001")
                        .build());
    }

    @Test
    public void assemble(){
        activityArmory.assembleActivitySku(9011L);
    }

    @Test
    public void test2() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setSku(9011L);
            skuRechargeEntity.setOutBusinessNumber(RandomStringUtils.randomNumeric(11));
            skuRechargeEntity.setUserId("user001");
            raffleActivity.createSkuRechargeOrder(skuRechargeEntity);
        }
    }
}
