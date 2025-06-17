package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.strategy.service.annotation.LogicStrategy;

import java.math.BigDecimal;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StrategyAward {
    Long id;
    Long strategyId;
    Integer awardId;
    String awardTitle;
    String awardSubtitle;
    Integer awardCount;
    Integer awardCountSurplus;
    BigDecimal awardRate;
    String ruleModels;
    Integer sort;
    Date createTime;
    Date updateTime;
}
