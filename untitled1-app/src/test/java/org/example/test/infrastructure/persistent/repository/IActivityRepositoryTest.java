package org.example.test.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.repository.IActivityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IActivityRepositoryTest {
    @Autowired
    private IActivityRepository activityRepository;

    @Test
    public void queryActivityAccount(){
        System.out.println(activityRepository.queryActivityAccount("xiaofuge", 100301L));
    }

    @Test
    public void queryDayAccount(){
        System.out.println(activityRepository.queryActivityDayAccount("123", 901L, "2025-07"));
    }
}
