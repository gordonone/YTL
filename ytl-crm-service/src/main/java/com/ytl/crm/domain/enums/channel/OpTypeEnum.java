package com.ytl.crm.domain.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OpTypeEnum {
    ADD(1,"新增"),
    UPDATE(2,"修改"),
    DELETE(3,"删除"),
    DISABLE(4,"禁用"),
    ENABLE(5,"启用");

    private Integer code;
    private String desc;

}
