package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IstrategyDispatch;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {


    @Resource
    private IStrategyRepository repository;

    @Resource
    protected IstrategyDispatch dispatch;

    private Long userScore = 4500L;


    @Override
    public Integer logic(String userId, Long strategyId) {
        String ruleValue = repository.queryStrategyRuleEntityByNoAwardId(strategyId, ruleModel());
        Map<Long, String> map = getRuleWeightValues(ruleValue);

        if (null == map || map.isEmpty()) return null;
        List<Long> sortedKeys = new ArrayList<>(map.keySet());
        Collections.sort(sortedKeys);
        Long nextValue = sortedKeys.stream()
                .filter(key -> userScore >= key)
                .max(Comparator.naturalOrder())
                .orElse(null);
        if (null != nextValue) {
            int awardId = dispatch.getRandomAwardId(strategyId, nextValue.toString());
            log.info("抽奖责任链：权重接管 userId={}, strategyId={}, ruleModel={}, awardId={}", userId, strategyId, ruleModel(), awardId);
            return awardId;
        }

        log.info("抽奖责任链：权重接管 userId={}, strategyId={}, ruleModel={}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_weight";
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
