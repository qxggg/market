package org.example.domain.strategy.service.rule.chain.factory;

import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
