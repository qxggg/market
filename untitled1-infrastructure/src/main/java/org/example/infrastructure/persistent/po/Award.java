package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class Award {
    Long id;
    Integer awardId;
    String awardKey;
    String awardConfig;
    String awardDesc;
    Date createTime;
    Date updateTime;
}
