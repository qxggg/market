package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.StrategyRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IStrategyRuleDao {
    StrategyRule queryStrategyRuleListByStrategyId(StrategyRule strategyRule);
}
