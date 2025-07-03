package org.example.domain.strategy.service.rule.chain;

import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 *
 */
public interface ILogicChain extends ILogicChainArmoy{

    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);

}
