package org.example.domain.strategy.model.vo;

import lombok.*;
import org.example.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.example.types.common.Constants;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVo {

    private String ruleModels;

    public String[] raffleCenterRuleModel(){
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if (DefaultLogicFactory.logicModel.isCenter(ruleModelValue)) {
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }
}
