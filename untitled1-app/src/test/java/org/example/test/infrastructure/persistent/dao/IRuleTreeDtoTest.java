package org.example.test.infrastructure.persistent.dao;


import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.example.infrastructure.persistent.dao.IRuleTreeDao;
import org.example.infrastructure.persistent.dao.IRuleTreeNodeDao;
import org.example.infrastructure.persistent.dao.IRuleTreeNodeLineDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IRuleTreeDtoTest {

    @Autowired
    IRuleTreeDao ruleTreeDao;

    @Autowired
    IRuleTreeNodeDao ruleTreeNodeDao;

    @Autowired
    IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Test
    public void RuleTreeDaoTest() {
        System.out.println(ruleTreeDao.queryRuleTreeByTreeId("tree_lock"));
    }

    @Test
    public void RuleTreeNodeDaoTest() {
        System.out.println(ruleTreeNodeDao.queryRuleTreeNodeListByTreeId("tree_lock"));
    }

    @Test
    public void RuleTreeNodeLineTest(){
        System.out.println(ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId("tree_lock"));
    }

}
