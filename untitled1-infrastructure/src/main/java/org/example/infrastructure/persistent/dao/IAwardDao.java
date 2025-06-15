package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Award;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IAwardDao {
   List<Award> queryList();
}
