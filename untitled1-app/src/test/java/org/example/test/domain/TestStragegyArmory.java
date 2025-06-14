package org.example.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.domain.strategy.service.armory.IstrategyDispatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class TestStragegyArmory {
    @Autowired
    IStrategyArmory strategyArmory;

    @Autowired
    IstrategyDispatch strategyDispatch;

    @Autowired
    IstrategyDispatch dispatch;
    @Resource
    private IStrategyRepository repository;

    @Test
    public void testSum() {
       strategyArmory.assembleLotteryStrategy(100001L);
   }

    @Test
    public void testInitStrategy() {
       System.out.println(repository.strategyAwardEntities(100001L));
   }

    @Test
    public void testGetRange(){
       System.out.println(repository.getRateRange(100001L));
   }

    @Test
    public void testGetStrategyRandomAward(){
        for (int i = 0; i < 10; i++) {
            System.out.println(strategyDispatch.getRandomAwardId(100001L, "4000:102, 103, 104, 105"));
        }

   }


}

