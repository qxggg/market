package org.example.domain.strategy.repository;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVo;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

    List<StrategyAwardEntity> strategyAwardEntities(Long strategyId);
    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRuleEntityByStrategyId(Long strategyId, String ruleModel);

    String queryStrategyRuleEntityByStrategyId(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleEntityByNoAwardId(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVo queryStrategyAwardRuleModelVoByStrategyId(Long strategyId, Integer awardId);

}
