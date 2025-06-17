package org.example.test.domain.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.StrategyArmoryDispatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class StrategyArmoryDispatchTest {

    @Autowired
    private StrategyArmoryDispatch strategyArmoryDispatch;

    @Autowired
    private IStrategyRepository repository;
    @Test
    public void testGenarateRuleValue(){
        int a = strategyArmoryDispatch.getRandomAwardId(100001L, "4000");
        System.out.println(a);
    }

    @Test
    public void testGenarateRuleValue2(){
        int rateRange = repository.getRateRange(100001L);
        System.out.println(rateRange);
    }
}
