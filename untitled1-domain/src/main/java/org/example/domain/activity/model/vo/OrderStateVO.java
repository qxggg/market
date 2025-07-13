package org.example.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStateVO {
    complete("complete", "完成");

    private final String code;
    private final String desc;
}
