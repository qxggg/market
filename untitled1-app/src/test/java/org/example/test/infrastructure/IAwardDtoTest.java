package org.example.test.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.dao.IAwardDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)

public class IAwardDtoTest {
    @Autowired
    IAwardDao awardDao;

    @Test
    public void test() {
        System.out.println(awardDao.queryList());
    }
}
