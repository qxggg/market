package org.example.infrastructure.persistent.po;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
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
