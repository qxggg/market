package org.example.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyRepository repository;

    private Long userRaffleCount = 0L;

    @Override
    public DefaultTreeNodeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        String ruleValue = repository.queryStrategyRuleEntityByStrategyId(strategyId, awardId, "rule_lock");
        if (ruleValue != null && userRaffleCount <= Long.parseLong(ruleValue)) {
            return DefaultTreeNodeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVo(RuleLogicCheckTypeVo.TAKE_OVER)
                    .build();
        }
        return DefaultTreeNodeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVo(RuleLogicCheckTypeVo.ALLOW)
                .build();
    }
}
