package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StrategyRule {
    Long strategyId;
    int id;
    int awardId;
    int ruleType;
    String ruleModel;
    String ruleValue;
    String ruleDesc;
    Date createTime;
    Date updateTime;
}
