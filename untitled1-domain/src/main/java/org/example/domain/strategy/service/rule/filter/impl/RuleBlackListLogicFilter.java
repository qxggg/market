package org.example.domain.strategy.service.rule.filter.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.annotation.LogicStrategy;
import org.example.domain.strategy.service.rule.filter.ILogicFilter;
import org.example.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.logicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤，黑名单 userId:{}, strategyId:{}, ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyID(), ruleMatterEntity.getRuleModel());
        String userId = ruleMatterEntity.getUserId();
        String ruleValue = repository.queryStrategyRuleEntityByStrategyId(ruleMatterEntity.getStrategyID(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);
        String[] userList = splitRuleValue[1].split(Constants.SPLIT);
        for (String user : userList) {
            if (userId.equals(user)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder().
                        ruleModel(DefaultLogicFactory.logicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyID())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVo.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVo.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder().
                code(RuleLogicCheckTypeVo.ALLOW.getCode())
                .info(RuleLogicCheckTypeVo.ALLOW.getInfo())
                .build();

    }


}
