package org.example.domain.strategy.service.armory;


public interface IStrategyArmory {

    boolean assembleLotteryStrategy(Long strategyId);
    public Integer getRandomAwardId(Long strategyId);
}
