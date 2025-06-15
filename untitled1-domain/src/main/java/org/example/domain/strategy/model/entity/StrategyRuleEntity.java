package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.types.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {
    Long strategyId;
    int awardId;
    int ruleType;
    String ruleModel;
    String ruleValue;
    String ruleDesc;

    public Map<String, List<Integer>> getRuleWeightValues(){
        if (!"rule_weight".equals(ruleModel)) return null;
        String[] temp = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> result = new HashMap<>();
        for (String str : temp) {
            String[] split = str.split(Constants.COLON);
            if (split.length != 2) throw new IllegalArgumentException("invalid input format (rule_weight)");
            String key = split[0];
            String values = split[1];
            String[] number = values.split(Constants.SPLIT);
            List<Integer> value = new ArrayList<>();
            for (int i = 0; i < number.length; i++) {
                value.add(Integer.parseInt(number[i]));
            }
            result.put(key, value);
        }
        return result;
    }
}
