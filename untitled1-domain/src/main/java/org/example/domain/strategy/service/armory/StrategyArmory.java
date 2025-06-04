package org.example.domain.strategy.service.armory;


import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class StrategyArmory implements IStrategyArmory {

    @Resource
    private IStrategyRepository repository;

    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        //1.查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.strategyAwardEntities(strategyId);

        //2.获得最小概率
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //3.获得概率总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //4. 用 1 % 0.0001 获得概率范围，百分位，千分位，万分位
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        ArrayList<Object> strategyAwardSearchRateTable = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++){
                strategyAwardSearchRateTable.add(awardId);
            }
        }
     }

}
