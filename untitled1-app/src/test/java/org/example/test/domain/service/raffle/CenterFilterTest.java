package org.example.test.domain.service.raffle;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.domain.strategy.service.raffle.AbstractRaffleStrategy;
import org.example.domain.strategy.service.rule.impl.RuleLockLogicFilter;
import org.example.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class CenterFilterTest {

    @Resource
    RuleLockLogicFilter lockLogicFilter;

    @Resource
    AbstractRaffleStrategy raffleStrategy;
    @Autowired
    private RuleWeightLogicFilter ruleWeightLogicFilter;
    @Autowired
    private RuleLockLogicFilter ruleLockLogicFilter;
    @Autowired
    IStrategyArmory strategyArmory;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 40500L);
        ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 0L);

        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100002L));
    }

    @Test
    public void testDoCentor(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .strategyId(100002L)
                .userId("user004")
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数 : {}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果 : {}", JSON.toJSONString(raffleAwardEntity));
    }






    /*

     public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userId:{}, strategyId:{}, awardId:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyID(), ruleMatterEntity.getAwardId());
        String ruleValue = repository.queryStrategyRuleEntityByStrategyId(ruleMatterEntity.getStrategyID(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
     */
}
