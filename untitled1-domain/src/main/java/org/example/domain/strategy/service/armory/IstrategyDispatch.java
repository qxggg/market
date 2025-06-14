package org.example.domain.strategy.service.armory;


public interface IstrategyDispatch {
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);


}
