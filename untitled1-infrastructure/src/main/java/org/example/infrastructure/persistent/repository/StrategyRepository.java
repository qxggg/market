package org.example.infrastructure.persistent.repository;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.*;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.po.*;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Resource
    IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    IRuleTreeDao ruleTreeDao;


    @Override
    public List<StrategyAwardEntity> strategyAwardEntities(Long strategyId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) return strategyAwardEntities;

        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }


    @Override
    public void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        System.out.println(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cache = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cache);
        if (null != strategyEntity) return strategyEntity;
        Strategy strategy = strategyDao.queryStrategyById(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        return strategyEntity;
    }

    public StrategyRuleEntity queryStrategyRuleEntityByStrategyId(Long strategyId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setRuleModel(ruleModel);
        StrategyRule strategyRuleRes = strategyRuleDao.queryStrategyRuleListByStrategyId(strategyRule);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRuleRes.getStrategyId())
                .awardId(strategyRuleRes.getAwardId())
                .ruleDesc(strategyRuleRes.getRuleDesc())
                .ruleModel(strategyRuleRes.getRuleModel())
                .ruleType(strategyRuleRes.getRuleType())
                .ruleValue(strategyRuleRes.getRuleValue())
                .build();
    }

    @Override
    public String queryStrategyRuleEntityByStrategyId(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setRuleModel(ruleModel);
        strategyRule.setAwardId(awardId);
        return strategyRuleDao.queryStrategyRuleValue(strategyRule);
    }

    @Override
    public String queryStrategyRuleEntityByNoAwardId(Long strategyId, String ruleModel) {
        return queryStrategyRuleEntityByStrategyId(strategyId, null, ruleModel);
    }

    @Override
    public StrategyAwardRuleModelVo queryStrategyAwardRuleModelVoByStrategyId(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        System.out.println(strategyId);
        System.out.println(awardId);
        return new StrategyAwardRuleModelVo(strategyAwardDao.queryStrategyRuleModel(strategyAward).getRuleModels());
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO vo = redisService.getValue(cacheKey);
        if (null != vo) return vo;
        List<RuleTreeNode> treeNode = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> treeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);

        Map<String, List<RuleTreeNodeLineVO>> nodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : treeNodeLines) {
            RuleTreeNodeLineVO lineVO = RuleTreeNodeLineVO.builder()
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleLimitValue(RuleLogicCheckTypeVo.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();
            List<RuleTreeNodeLineVO> tmpLineVO = nodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            tmpLineVO.add(lineVO);
        }

        Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : treeNode) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(nodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();
            treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
        }

        RuleTreeVO ruleTreeVO = RuleTreeVO
                .builder()
                .treeDesc(ruleTree.getTreeDesc())
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeNodeMap(treeNodeMap)
                .treeRootRuleNode(ruleTree.getTreeRootRuleKey())
                .build();

        redisService.setValue(cacheKey, ruleTreeVO);
        return ruleTreeVO;
    }

}
