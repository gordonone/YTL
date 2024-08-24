package com.ytl.crm.domain.enums.task.config;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionMaterialTypeEnum implements EnumWithCodeAndDesc<String> {
    TEXT("TEXT", "文本"),
    POSTER("POSTER", "海报"),
    QUESTIONNAIRE("QUESTIONNAIRE", "问卷"),
    JIA_FU_COUPON("JIA_FU_COUPON", "家服优惠券"),
    ;
    private final String code;
    private final String desc;
}
