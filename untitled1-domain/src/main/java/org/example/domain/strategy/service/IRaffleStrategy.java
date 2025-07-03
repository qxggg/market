package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;

public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);

    DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) ;

    DefaultTreeNodeFactory.StrategyAwardData raffleLogicTree(String userId, Long strategyId, Integer awardId) ;
}
