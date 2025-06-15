package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardEntity {

    private long strategyId;
    private int awardId;
    private BigDecimal awardRate;
    private String awardSubtitle;
    private int awardCount;
    private int awardCountSurplus;
}
