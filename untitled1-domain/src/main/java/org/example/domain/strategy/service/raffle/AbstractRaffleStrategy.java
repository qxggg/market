package org.example.domain.strategy.service.raffle;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.strategy.model.entity.*;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.IRaffleStock;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.armory.IstrategyDispatch;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeNodeFactory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy, IRaffleStock {

    protected IStrategyRepository strategyRepository;

    protected IstrategyDispatch dispatch;

    protected DefaultChainFactory chainFactory;

    protected DefaultTreeNodeFactory treeFactory;


    @Autowired
    public AbstractRaffleStrategy(IStrategyRepository repository, IstrategyDispatch dispatch, DefaultChainFactory chainFactory, DefaultTreeNodeFactory treeFactory) {
        this.strategyRepository = repository;
        this.dispatch = dispatch;
        this.chainFactory = chainFactory;
        this.treeFactory = treeFactory;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1 先判空
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 责任链抽奖计算【这步拿到的是初步的抽奖ID，之后需要根据ID处理抽奖】注意；黑名单、权重等非默认抽奖的直接返回抽奖结果
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        if (!DefaultChainFactory.logicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
            return buildRaffleAwardEntity(strategyId, chainStrategyAwardVO.getAwardId(), null);
        }

        // 3. 规则树抽奖过滤【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
        DefaultTreeNodeFactory.StrategyAwardData treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        //4 返回结果
        return buildRaffleAwardEntity(strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());


//        //2。责任链处理抽奖
//
//        System.out.println(chainFactory);
//        ILogicChain logicChain = chainFactory.getLogicChain(strategyId);
//        Integer awardId = logicChain.logic(userId, strategyId).getAwardId();
//
//
//       //3.查询奖品规则（抽奖中过滤），如果没有库存返回兜底奖励
//        if (awardId == 100) return RaffleAwardEntity.builder().awardId(awardId)
//                .awardDesc("黑名单拦截，返回100黑名单奖品")
//                .build();
//        StrategyAwardRuleModelVo strategyAwardRuleModelVo = strategyRepository.queryStrategyAwardRuleModelVoByStrategyId(strategyId, awardId);

//        //4 抽奖中规则过滤
//        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntityCenter = doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
//                .userId(userId)
//                .strategyId(strategyId).
//                awardId(awardId).build(),strategyAwardRuleModelVo.raffleCenterRuleModel());
//        //判等
//        if (RuleLogicCheckTypeVo.TAKE_OVER.getCode().equals(ruleActionEntityCenter.getCode())){
//            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励");
//            return RaffleAwardEntity.builder()
//                    .awardDesc("规则拦截， 通过抽奖后走兜底奖励")
//                    .build();
//        }



    }
    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId, Integer awardId, String awardConfig) {
        StrategyAwardEntity strategyAward = strategyRepository.queryStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardConfig(awardConfig)
                .sort(strategyAward.getSort())
                .build();
    }
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) ;

    public abstract DefaultTreeNodeFactory.StrategyAwardData raffleLogicTree(String userId, Long strategyId, Integer awardId) ;
 //   protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String ...logics);

 //   protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String ...logics);

}
