package org.example.domain.strategy.service.raffle;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.armory.IstrategyDispatch;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository strategyRepository;

    protected IstrategyDispatch dispatch;

    private DefaultChainFactory chainFactory;

    @Autowired
    public AbstractRaffleStrategy(IStrategyRepository repository, IstrategyDispatch dispatch, DefaultChainFactory chainFactory) {
        this.strategyRepository = repository;
        this.dispatch = dispatch;
        this.chainFactory = chainFactory;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //2。责任链处理抽奖

        System.out.println(chainFactory);
        ILogicChain logicChain = chainFactory.getLogicChain(strategyId);
        Integer awardId = logicChain.logic(userId, strategyId);

//
//        //查询策略id
//        StrategyEntity strategy = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
//
//        //3. 抽奖前过滤
//        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());
//        if (RuleLogicCheckTypeVo.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
//            if (DefaultLogicFactory.logicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
//                //黑名单返回固定的奖品id就行了。
//                return RaffleAwardEntity.builder()
//                        .awardId(ruleActionEntity.getData().getAwardId())
//                        .build();
//            }
//            else if (DefaultLogicFactory.logicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())){
//                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
//                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
//                Integer awardId = dispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
//                return RaffleAwardEntity.builder().awardId(awardId).build();
//            }
//        }
//
//        //4.走默认流程
//        Integer awardId = dispatch.getRandomAwardId(strategyId);
//
//       //3.查询奖品规则（抽奖中过滤），如果没有库存返回兜底奖励
        if (awardId == 100) return RaffleAwardEntity.builder().awardId(awardId)
                .awardDesc("黑名单拦截，返回100黑名单奖品")
                .build();
        StrategyAwardRuleModelVo strategyAwardRuleModelVo = strategyRepository.queryStrategyAwardRuleModelVoByStrategyId(strategyId, awardId);

        //4 抽奖中规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntityCenter = doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId).
                awardId(awardId).build(),strategyAwardRuleModelVo.raffleCenterRuleModel());
        //判等
        if (RuleLogicCheckTypeVo.TAKE_OVER.getCode().equals(ruleActionEntityCenter.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励");
            return RaffleAwardEntity.builder()
                    .awardDesc("规则拦截， 通过抽奖后走兜底奖励")
                    .build();
        }

        return RaffleAwardEntity.builder().awardId(awardId).build();



    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String ...logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String ...logics);

}
