package org.example.domain.strategy.service.rule.tree.factory.engine;

import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;

public interface IDecisionTreeNodeEngine {
    DefaultTreeNodeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId);
}
