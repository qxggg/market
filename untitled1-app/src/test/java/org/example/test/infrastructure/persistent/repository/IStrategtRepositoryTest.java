package org.example.test.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IStrategtRepositoryTest {
    @Autowired
    IStrategyRepository repository;

    @Test
    public void testGetStrategyEntities(){
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(100001L);
        System.out.println(strategyEntity.getRuleModels());
    }

    @Test
    public void testQueryStrategyEntityRuleByStrategyId(){
        StrategyRuleEntity strategyEntity = repository.queryStrategyRuleEntityByStrategyId(100001L, "rule_random");
        System.out.println(strategyEntity);
    }

    @Test
    public void testQueryRuleModelsByStrategyId(){
        System.out.println(repository.queryStrategyAwardRuleModelVoByStrategyId(100001L, 101));
    }

    @Test
    public void testqueryStrategyRuleEntityByStrategyId(){
        System.out.println(repository.queryStrategyRuleEntityByStrategyId(100001L, 101, "rule_lock"));
    }
}
