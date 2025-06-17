package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class StrategyRule {
    Long strategyId;
    Integer id;
    Integer awardId;
    Integer ruleType;
    String ruleModel;
    String ruleValue;
    String ruleDesc;
    Date createTime;
    Date updateTime;
}
