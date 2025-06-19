package org.example.test.domain.service.rule.chain;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class ChainFactoryTest {

    @Autowired
    DefaultChainFactory factory;

    @Test
    public void test() {
        ILogicChain logicChain = factory.getLogicChain(100002L);
        System.out.println(logicChain);
    }
}

