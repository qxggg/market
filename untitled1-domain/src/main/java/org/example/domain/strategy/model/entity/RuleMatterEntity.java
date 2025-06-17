package org.example.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleMatterEntity {

    private String userId;
    private Long strategyID;
    private Integer awardId;
    private String ruleModel;

}
