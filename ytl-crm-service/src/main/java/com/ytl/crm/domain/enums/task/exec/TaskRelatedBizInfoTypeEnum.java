package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskRelatedBizInfoTypeEnum {

    RENT_CONTRACT("RENT_CONTRACT", "租客合同"),
    ;

    private final String code;

    private final String desc;

}
