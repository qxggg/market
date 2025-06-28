package org.example.domain.strategy.service.rule.tree.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.model.vo.RuleTreeVO;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeNodeEngine;
import org.example.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeNodeEngine;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultTreeNodeFactory {

    private final Map<String, ILogicTreeNode> iLogicTreeNodeMap;

    public DefaultTreeNodeFactory(Map<String, ILogicTreeNode> iLogicTreeNodeMap) {
        this.iLogicTreeNodeMap = iLogicTreeNodeMap;
    }

    public IDecisionTreeNodeEngine openLogicTree(RuleTreeVO vo){
        return new DecisionTreeNodeEngine(iLogicTreeNodeMap, vo);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardData{
        private Integer awardId;
        private String awardRuleValue;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity{
        private RuleLogicCheckTypeVo ruleLogicCheckTypeVo;
        private StrategyAwardData strategyAwardData;
    }
}
