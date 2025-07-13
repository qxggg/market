package org.example.test.infrastructure.persistent.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.example.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import org.example.infrastructure.persistent.po.RaffleActivityOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityOrderDaoTest {

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;

    @Test
    public void test_insert(){
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId("xiaofuge");
        raffleActivityOrder.setActivityId(100301L);
        raffleActivityOrder.setActivityName("测试活动");
        raffleActivityOrder.setStrategyId(100006L);
        raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        raffleActivityOrder.setOrderTime(new Date());
        raffleActivityOrder.setState("not_used");
        raffleActivityOrder.setSku(0L);
        raffleActivityOrder.setDayCount(1);
        raffleActivityOrder.setMonthCount(1);
        raffleActivityOrder.setTotalCount(2);        // 插入数据
        raffleActivityOrderDao.insert(raffleActivityOrder);
    }

    @Test
    public void query(){
        System.out.println(raffleActivityOrderDao.queryRaffleActivityOrderByUserId("xiaofuge"));
    }
}
