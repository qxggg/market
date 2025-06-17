package org.example.domain.strategy.service.rule.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.annotation.LogicStrategy;
import org.example.domain.strategy.service.rule.ILogicFilter;
import org.example.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.logicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    private Long userRaffleCount = 0L;
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userId:{}, strategyId:{}, awardId:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyID(), ruleMatterEntity.getAwardId());
        String ruleValue = repository.queryStrategyRuleEntityByStrategyId(ruleMatterEntity.getStrategyID(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        if (userRaffleCount > Long.parseLong(ruleValue)) {
           return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                   .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                   .info(RuleLogicCheckTypeVo.ALLOW.getInfo())
                   .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVo.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVo.TAKE_OVER.getInfo())
                .build();
    }
}
