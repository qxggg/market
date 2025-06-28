package org.example.domain.strategy.service.rule.tree;

import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;

public interface ILogicTreeNode {
    DefaultTreeNodeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId);
}
