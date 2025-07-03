package org.example.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IstrategyDispatch;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IstrategyDispatch strategyDispatch;
    @Resource
    private IStrategyRepository strategyRepository;
    @Override
    public DefaultTreeNodeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤，库存过滤，userid:{},strategyId:{},awardId:{}", userId, strategyId, awardId);
        Boolean status = strategyDispatch.subtractionAwardStock(strategyId, awardId);
        if (status) {
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build());
            return DefaultTreeNodeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVo(RuleLogicCheckTypeVo.ALLOW)
                    .strategyAwardData(DefaultTreeNodeFactory.StrategyAwardData.builder()
                            .awardId(awardId)
                            .awardRuleValue("")
                            .build())
                    .build();

        }
        return DefaultTreeNodeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVo(RuleLogicCheckTypeVo.TAKE_OVER)
                .build();
    }
}
