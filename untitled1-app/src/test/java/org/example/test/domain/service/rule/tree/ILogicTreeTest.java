package org.example.test.domain.service.rule.tree;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.*;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeNodeEngine;
import org.example.infrastructure.persistent.repository.StrategyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class ILogicTreeTest {

    @Resource
    DefaultTreeNodeFactory defaultTreeFactory;

    @Autowired
    StrategyRepository repository;
    @Test
    public void test_tree_rule() {
        // 构建参数
        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
                .treeId("10000001")
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("10000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVo.TAKE_OVER)
                            .build());

                    add(RuleTreeNodeLineVO.builder()
                            .treeId("10000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_stock")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVo.ALLOW)
                            .build());
                }})
                .build();

        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
                .treeId("10000001")
                .ruleKey("rule_luck_award")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(null)
                .build();

        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
                .treeId("10000001")
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue(null)
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("10000001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVo.TAKE_OVER)
                            .build());
                }})
                .build();

        RuleTreeVO ruleTreeVO = new RuleTreeVO();
        ruleTreeVO.setTreeId("10000001");
        ruleTreeVO.setTreeName("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeRootRuleNode("rule_lock");

        ruleTreeVO.setTreeNodeMap(new HashMap<String, RuleTreeNodeVO>() {{
            put("rule_lock", rule_lock);
            put("rule_stock", rule_stock);
            put("rule_luck_award", rule_luck_award);
        }});

        IDecisionTreeNodeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);

        DefaultTreeNodeFactory.StrategyAwardData data = treeEngine.process("xiaofuge", 100001L, 100);
        log.info("测试结果：{}", JSON.toJSONString(data));

    }

    @Test
    public void test(){
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId("tree_lock");
        IDecisionTreeNodeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        DefaultTreeNodeFactory.StrategyAwardData data = treeEngine.process("xiaofuge", 100001L, 100);
        log.info("测试结果：{}", JSON.toJSONString(data));
    }


}
