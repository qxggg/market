package org.example.test.domain.service.raffle;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.service.raffle.AbstractRaffleStrategy;
import org.example.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class PreFilterTest {

    @Resource
    @Qualifier("defaultRaffleStrategy")
    AbstractRaffleStrategy raffleStrategy;
    @Autowired
    private RuleWeightLogicFilter ruleWeightLogicFilter;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 6500L);
    }

    @Test
    public void preFilterTest() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .strategyId(100001L)
                .userId("user003")
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数 : {}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果 : {}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void ruleWeightTest(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .strategyId(100001L)
                .userId("user007")
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数 : {}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果 : {}", JSON.toJSONString(raffleAwardEntity));
    }
}
