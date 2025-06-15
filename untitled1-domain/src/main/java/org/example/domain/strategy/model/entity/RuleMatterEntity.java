package org.example.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RuleMatterEntity {

    private String userId;
    private Long strategyID;
    private Integer awardId;
    private String ruleModel;

}
