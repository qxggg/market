package org.example.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StrategyAward {
    long id;
    long strategyId;
    int awardId;
    String awardTitle;
    String awardSubtitle;
    int awardCount;
    int awardCountSurplus;
    BigDecimal awardRate;
    String ruleModels;
    int sort;
    Date createTime;
    Date updateTime;
}
