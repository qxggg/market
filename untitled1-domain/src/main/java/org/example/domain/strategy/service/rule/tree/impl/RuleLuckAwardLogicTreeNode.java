package org.example.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeNodeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeNodeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVo(RuleLogicCheckTypeVo.TAKE_OVER)
                .strategyAwardData(DefaultTreeNodeFactory.StrategyAwardData.builder()
                        .awardId(101)
                        .awardRuleValue("1, 100")
                        .build())
                .build();
    }
}
