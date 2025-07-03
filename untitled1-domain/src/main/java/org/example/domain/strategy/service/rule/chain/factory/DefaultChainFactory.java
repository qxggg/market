package org.example.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChains;

    private IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChains, IStrategyRepository repository) {
        this.logicChains = logicChains;
        this.repository = repository;
    }

    public ILogicChain getLogicChain(Long strategyId) {
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        if (strategy == null)  return logicChains.get("default");
        String[] ruleModels = strategy.ruleModels();

        if (null == ruleModels || ruleModels.length == 0) return logicChains.get("default");
        ILogicChain logicChain = logicChains.get(ruleModels[0]);
        ILogicChain currentLogicChain = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextLogicChain = logicChains.get(ruleModels[i]);
            currentLogicChain = currentLogicChain.appendNext(nextLogicChain);
        }
        currentLogicChain.appendNext(logicChains.get("default"));
        return logicChain;
    }

    @Getter
    @AllArgsConstructor
    public enum logicModel{

        RULE_WIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单直接返回"),
        RULE_DEFAULT("rule_default", "【抽奖后规则】幸运奖品");
        private final String code;
        private final String info;
    }

    @Getter
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class StrategyAwardVO{
        private Integer awardId;
        private String logicModel;
    }

}
