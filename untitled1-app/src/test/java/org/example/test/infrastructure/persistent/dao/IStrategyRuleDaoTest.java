package org.example.test.infrastructure.persistent.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.dao.IStrategyRuleDao;
import org.example.infrastructure.persistent.po.StrategyRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IStrategyRuleDaoTest {
    @Autowired
    IStrategyRuleDao strategyRuleDao;

    @Test
    public void queryRuleListById() {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setRuleModel("rule_random");
        strategyRule.setStrategyId(100001L);
        System.out.println(strategyRuleDao.queryStrategyRuleListByStrategyId(strategyRule));
    }

    @Test
    public void queryRuleValue(){
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setRuleModel("rule_blacklist");
        strategyRule.setStrategyId(100001L);
        strategyRule.setAwardId(120);
        System.out.println(strategyRuleDao.queryStrategyRuleValue(strategyRule));
    }
}
