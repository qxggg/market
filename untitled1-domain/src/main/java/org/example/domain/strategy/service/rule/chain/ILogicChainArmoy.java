package org.example.domain.strategy.service.rule.chain;

public interface ILogicChainArmoy {

    ILogicChain appendNext(ILogicChain next);

    ILogicChain next();
}

