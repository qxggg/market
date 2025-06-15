package org.example.test.domain.entity;


import lombok.extern.slf4j.Slf4j;
import org.example.types.common.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class StrategyRuleEntitiesTest {

    @Test
    public void testHandleString(){
        String ruleValue = "4000:102,103,104,105 6000:102,103,104,105,106,107,108,109";
        String[] temp = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> result = new HashMap<>();
        for (String str : temp) {
            String[] split = str.split(Constants.COLON);
            if (split.length != 2) throw new IllegalArgumentException("invalid input format (rule_weight)");
            String key = split[0];
            String values = split[1];
            String[] number = values.split(Constants.SPLIT);
            List<Integer> value = new ArrayList<>();
            for (int i = 0; i < number.length; i++) {
                value.add(Integer.parseInt(number[i]));
            }
            result.put(key, value);
        }
        System.out.println(result);
    }
}
