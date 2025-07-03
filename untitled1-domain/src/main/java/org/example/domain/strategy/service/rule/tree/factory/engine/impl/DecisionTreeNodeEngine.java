package org.example.domain.strategy.service.rule.tree.factory.engine.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.model.vo.RuleTreeNodeLineVO;
import org.example.domain.strategy.model.vo.RuleTreeNodeVO;
import org.example.domain.strategy.model.vo.RuleTreeVO;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeNodeEngine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
public class DecisionTreeNodeEngine implements IDecisionTreeNodeEngine {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeNodeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeNodeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId) {
        String root = ruleTreeVO.getTreeRootRuleNode();
        DefaultTreeNodeFactory.StrategyAwardData strategyAwardData = null;
        Map<String, RuleTreeNodeVO> nodeVOMap = ruleTreeVO.getTreeNodeMap();
        RuleTreeNodeVO next = nodeVOMap.get(root);
        while (next != null) {
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(next.getRuleKey());
            String ruleValue = next.getRuleValue();
            DefaultTreeNodeFactory.TreeActionEntity entity = logicTreeNode.logic(userId, strategyId, awardId, ruleValue);
            RuleLogicCheckTypeVo vo = entity.getRuleLogicCheckTypeVo();
            strategyAwardData = entity.getStrategyAwardData();
            String nextString = next(vo.getCode(), next.getTreeNodeLineVOList());
          //  log.info("决策树引擎【{}】: treeId:{},node:{},code:{}", ruleTreeVO.getTreeName(), next.getTreeId(), nextString, vo.getCode());
            next = nodeVOMap.get(nextString);
        }
        return strategyAwardData;
    }
    String next(String ruleMatter, List<RuleTreeNodeLineVO> list){
        System.out.println(list);
        if (list == null || list.isEmpty()) return null;
        for (RuleTreeNodeLineVO ruleTreeNodeLineVO : list) {
            if (decisionLogic(ruleMatter, ruleTreeNodeLineVO)){
                return ruleTreeNodeLineVO.getRuleNodeTo();
            }
        }
       // throw new RuntimeException("决策树引擎，未找到可执行节点");
        return null;
    }

    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }


/*
    @Override
    public DefaultTreeNodeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId) {
        DefaultTreeNodeFactory.StrategyAwardData strategyAwardData = null;
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        //获得所有的节点
        Map<String, RuleTreeNodeVO> treeVOMap = ruleTreeVO.getTreeNodeMap();
        //从根节点开始遍历
        RuleTreeNodeVO ruleTreeNode = treeVOMap.get(nextNode);

        while (nextNode != null) {
            //从所有的规则节点里面进行遍历
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(nextNode);
            DefaultTreeNodeFactory.TreeActionEntity treeActionEntity = logicTreeNode.logic(userId, strategyId, awardId);
            RuleLogicCheckTypeVo ruleLogicCheckTypeVo = treeActionEntity.getRuleLogicCheckTypeVo();
            strategyAwardData = treeActionEntity.getStrategyAwardData();
            log.info("决策树引擎【{}】: treeId:{},node:{},code:{}", ruleTreeVO.getTreeName(), ruleTreeNode.getTreeId(), nextNode, ruleLogicCheckTypeVo.getCode());
            nextNode = nextNode(ruleLogicCheckTypeVo.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            //找到下一个节点
            ruleTreeNode = treeVOMap.get(nextNode);
        }
        return strategyAwardData;
    }

    private String nextNode(String matterValue, List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList){
        if (null == ruleTreeNodeLineVOList || ruleTreeNodeLineVOList.isEmpty()) {
            return null;
        }
        for (RuleTreeNodeLineVO nodeLineVO : ruleTreeNodeLineVOList){
            if (decisionLogic(matterValue, nodeLineVO)) {
                return nodeLineVO.getRuleNodeTo();
            }
        }
        throw new RuntimeException("决策树引擎，未找到可执行节点");
    }
*/
}
