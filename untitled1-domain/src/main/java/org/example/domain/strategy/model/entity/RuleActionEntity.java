package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity <T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVo.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVo.ALLOW.getInfo();
    private String ruleModel;
    private T data;


    static public class RaffleEntity{

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    //抽奖之前
    static public class RaffleBeforeEntity extends RaffleEntity{
        private Long strategyId;

        private String ruleWeightValueKey;

        private int awardId;
    }


    //抽奖中
    static public class RaffleCenterEntity extends RaffleEntity{

    }


    //抽奖后
    static public class RaffleAfterEntity extends RaffleEntity{

    }
}
