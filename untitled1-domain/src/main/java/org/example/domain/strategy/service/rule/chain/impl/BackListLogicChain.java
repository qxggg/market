package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
public class BackListLogicChain extends AbstractLogicChain {

    @Resource
    IStrategyRepository repository;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链—黑名单, userId={}, strategyId={}, ruleModel={}", userId, strategyId, ruleModel());
        String ruleValue = repository.queryStrategyRuleEntityByNoAwardId(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);
        String[] userList = splitRuleValue[1].split(Constants.SPLIT);
        for (String user : userList){
            if (userId.equals(user)){
                log.info("抽奖责任链：黑名单接管 userId={}, strategyId={}, ruleModel={}, awardId={}", userId, strategyId, ruleModel(), awardId);
                return awardId;
            }
        }

        log.info("抽奖责任链：黑名单放行 userId={}, strategyId={}, ruleModel={}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
