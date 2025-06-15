package org.example.domain.strategy.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.annotation.LogicStrategy;
import org.example.domain.strategy.service.rule.ILogicFilter;
import org.example.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.logicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    private Long userScore = 4500L;


    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String ruleValue = repository.queryStrategyRuleEntityByStrategyId(ruleMatterEntity.getStrategyID(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        System.out.println(ruleValue);
        Map<Long, String> map = getRuleWeightValues(ruleValue);
        System.out.println(map);

        if (null == map || map.isEmpty()) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                    .code(RuleLogicCheckTypeVo.ALLOW.getInfo())
                    .build();
        }

        List<Long> sortedKeys = new ArrayList<>(map.keySet());
        System.out.println(sortedKeys);

        Long nextValue = sortedKeys.stream()
                .filter(key -> userScore >= key)
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (null != nextValue) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(ruleMatterEntity.getStrategyID())
                            .ruleWeightValueKey(map.get(nextValue))
                            .build())
                    .ruleModel(DefaultLogicFactory.logicModel.RULE_WIGHT.getCode())
                    .code(RuleLogicCheckTypeVo.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVo.TAKE_OVER.getInfo())
                    .build();

        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                .code(RuleLogicCheckTypeVo.ALLOW.getInfo())
                .build();
    }

    public Map<Long, String> getRuleWeightValues(String ruleValue){
        String[] temp = ruleValue.split(Constants.SPACE);
        Map<Long, String> result = new HashMap<>();
        for (String str : temp) {
            String[] split = str.split(Constants.COLON);
            if (split.length != 2) throw new IllegalArgumentException("invalid input format (rule_weight)");
            String key = split[0];
            String values = split[1];
            result.put(Long.parseLong(key), values);
        }
        return result;
    }
}


