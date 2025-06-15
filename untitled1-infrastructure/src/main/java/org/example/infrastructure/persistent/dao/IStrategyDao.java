package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Strategy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IStrategyDao {
    Strategy queryStrategyById(Long strategyId);
}
