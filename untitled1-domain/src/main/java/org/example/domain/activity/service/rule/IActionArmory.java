package org.example.domain.activity.service.rule;

public interface IActionArmory {
    IActionChain next();

    IActionChain appendNext(IActionChain next);
}
