package org.example.test.domain.service.rule.factory;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.service.rule.filter.ILogicFilter;
import org.example.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class LogicFactoryBeanTest {

    @Resource
    DefaultLogicFactory logicFactory;

    @Test
    public void testLogicFactoryBean() {
        java.util.Map<String, ILogicFilter<RuleActionEntity.RaffleEntity>> map = logicFactory.openLogicFilter();
        System.out.println(map.get("rule_blacklist"));
    }

}
