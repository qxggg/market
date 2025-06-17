package org.example.test.infrastructure.persistent.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.dao.IStrategyAwardDao;
import org.example.infrastructure.persistent.po.StrategyAward;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IStrategyAwardDaoTest {

    @Autowired
    private IStrategyAwardDao strategyAwardDao;

    @Test
    public void testGetRuleModel(){
        StrategyAward str = new StrategyAward();
        str.setAwardId(107);
        str.setStrategyId(100002L);
        System.out.println(strategyAwardDao.queryStrategyRuleModel(str));
    }
}
