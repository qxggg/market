package org.example.test.infrastructure.persistent.dao;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.infrastructure.persistent.dao.IRaffleActivityAccountDao;
import org.example.infrastructure.persistent.dao.IRaffleActivityAccountDayDao;
import org.example.infrastructure.persistent.dao.IRaffleActivityAccountMonthDao;
import org.example.infrastructure.persistent.dao.IRaffleActivityDao;
import org.example.infrastructure.persistent.po.RaffleActivityAccount;
import org.example.infrastructure.persistent.po.RaffleActivityAccountDay;
import org.example.infrastructure.persistent.po.RaffleActivityAccountMonth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityAccountDao {

    @Autowired
    IRaffleActivityAccountDao raffleActivityAccountDao;

    @Autowired
    IRaffleActivityAccountDayDao raffleActivityAccountDayDao;

    @Autowired
    IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;

    @Test
    public void query(){
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setActivityId(100301L);
        raffleActivityAccount.setUserId("xiaofuge");
        System.out.println(raffleActivityAccountDao.queryActivityAccount(raffleActivityAccount));
    }

    @Test
    public void queryDay(){
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setActivityId(901L);
        raffleActivityAccountDay.setUserId("123");
        raffleActivityAccountDay.setDay("3");
        System.out.println(raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDay));
    }

    @Test
    public void queryMonth(){
        RaffleActivityAccountMonth raffleActivityAccountMonth = new RaffleActivityAccountMonth();
        raffleActivityAccountMonth.setActivityId(901L);
        raffleActivityAccountMonth.setUserId("123");
        raffleActivityAccountMonth.setMonth("3");
        System.out.println(raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(raffleActivityAccountMonth));
    }


}
