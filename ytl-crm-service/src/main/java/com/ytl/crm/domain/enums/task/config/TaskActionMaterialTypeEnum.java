package com.ytl.crm.domain.enums.task.config;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Deprecated
public enum TaskActionMaterialTypeEnum implements EnumWithCodeAndDesc<String> {
    TEXT("TEXT", "文本", 2),
    POSTER("POSTER", "海报", 1),
    ARTICLE("ARTICLE", "文章", 4),
    PICTURE("PICTURE", "图片", 3),
    QUESTIONNAIRE("QUESTIONNAIRE", "问卷", 8),
    JIA_FU_COUPON("JIA_FU_COUPON", "家服优惠券", 8),
    ;
    private final String code;
    private final String desc;
    private final int wsType;
}
