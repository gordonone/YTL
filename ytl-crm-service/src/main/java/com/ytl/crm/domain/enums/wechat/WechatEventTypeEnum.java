package com.ytl.crm.domain.enums.wechat;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatEventTypeEnum implements EnumWithCodeAndDesc<String> {
    ADD_FRIEND("ADD_FRIEND", "新增好友"),
    DEL_FRIEND("DEL_FRIEND", "删除好友"),
    EDIT_FRIEND("EDIT_FRIEND", "编辑好友"),
    ;
    private final String code;
    private final String desc;
}
