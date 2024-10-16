package com.ytl.crm.domain.enums.wechat;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatFriendStatusEnum implements EnumWithCodeAndDesc<Integer> {
    ADDED(0, "已添加"),
    DELETED(1, "已删除");;
    private final Integer code;
    private final String desc;

}
