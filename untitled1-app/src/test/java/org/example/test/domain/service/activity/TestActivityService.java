package org.example.test.domain.service.activity;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.AbstractRaffleActivity;
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
    AbstractRaffleActivity raffleActivity;
    @Test
    public void test() {
        raffleActivity.createOrder(ActivityShopCartEntity.builder()
                        .sku(9011L)
                        .userId("user001")
                        .build());
    }
}
