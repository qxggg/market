package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.StrategyAward;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    StrategyAward queryStrategyRuleModel(StrategyAward strategyAward);


}
