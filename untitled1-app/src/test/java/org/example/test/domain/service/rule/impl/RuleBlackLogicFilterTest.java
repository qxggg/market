package org.example.test.domain.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;
import org.example.domain.strategy.service.rule.ILogicFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RuleBlackLogicFilterTest {

    @Resource
    @Qualifier("ruleBlackListLogicFilter")
    ILogicFilter<RuleActionEntity.RaffleBeforeEntity> iLogicFilter;

    @Test
    public void test() {
        RuleMatterEntity ruleMatterEntity = new RuleMatterEntity("user001", 100001L, 120, "rule_blacklist");
        System.out.println(iLogicFilter.filter(ruleMatterEntity));
    }
}
