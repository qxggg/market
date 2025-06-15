package org.example.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.armory.IstrategyDispatch;

@Slf4j
public class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository strategyRepository;

    protected IstrategyDispatch dispatch;
    @Override
    public void performRaffle(RaffleFactorEntity raffleFactorEntity) {


    }
}
