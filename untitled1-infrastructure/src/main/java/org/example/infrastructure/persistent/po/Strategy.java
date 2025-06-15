package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Strategy {
    Long strategyId;
    int id;
    String strategyDesc;
    String ruleModels;
    Date createTime;
    Date updateTime;


}
