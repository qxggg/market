package org.example.domain.strategy.service.rule.filter.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.service.annotation.LogicStrategy;
import org.example.domain.strategy.service.rule.filter.ILogicFilter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();


    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logicFilter -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logicFilter.getClass(), LogicStrategy.class);
            if (strategy != null) {
                logicFilterMap.put(strategy.logicModel().getCode(), logicFilter);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter(){
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }



    @Getter
    @AllArgsConstructor
    public enum logicModel{

        RULE_WIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY", "before"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单直接返回", "before"),
        RULE_LOCK("rule_lock", "【抽奖中规则】对应奖品可以解锁抽奖", "center"),
        RULE_LUCK_AWARD("rule_luck", "【抽奖后规则】幸运奖品", "after");
        private final String code;
        private final String info;
        private final String type;

        public static boolean isCenter(String code){
            return "center".equals(logicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isAfter(String code){
            return "after".equals(logicModel.valueOf(code.toUpperCase()).type);
        }

    }
}
