package org.example.test;

import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IRedisService redisService;

    @Test
    public void test() {
        log.info("测试完成");
    }

    @Test
    public void redisTest(){
        RMap<Object, Object> map = redisService.getMap("qxg");
        map.put(1, 101);
        map.put(2, 101);
        map.put(3, 101);
        map.put(4, 101);
        map.put(5, 102);
        map.put(6, 103);
        map.put(7, 104);
        log.info(redisService.getFromMap("qxg", 7).toString());
        log.info(redisService.getValue("123"));
    }
    @Test
    public void testDatabase(){
        jdbcTemplate.execute("select * from test");
    }
}
