package org.example.test.infrastructure.persistent.dao;


import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.dao.IRaffleActivityCountDao;
import org.example.infrastructure.persistent.dao.IRaffleActivityDao;
import org.example.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityDaoTest {

    @Autowired
    private IRaffleActivityDao raffleActivityDao;

    @Autowired
    private IRaffleActivityCountDao raffleActivityCountDao;

    @Autowired
    private IRaffleActivitySkuDao raffleActivitySkuDao;

    @Test
    public void test(){
        System.out.println(raffleActivityDao.queryRaffleActivityByActivityId(100301L));
    }

    @Test
    public void testSku(){
        System.out.println(raffleActivitySkuDao.queryActivitySku(9011L));
    }

    @Test
    public void testCount(){
        System.out.println(raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(11101L));
    }
}
