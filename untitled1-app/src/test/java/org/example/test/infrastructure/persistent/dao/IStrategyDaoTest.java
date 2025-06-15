package org.example.test.infrastructure.persistent.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.dao.IStrategyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IStrategyDaoTest {

    @Autowired
    private IStrategyDao strategyDao;

    @Test
    public void testGetStrategyById() {
        System.out.println(strategyDao.queryStrategyById(100001L).toString());
    }
}
