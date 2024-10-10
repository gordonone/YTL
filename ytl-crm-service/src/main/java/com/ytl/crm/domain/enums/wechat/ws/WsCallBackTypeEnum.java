package com.ytl.crm.domain.enums.wechat.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum WsCallBackTypeEnum {

    /**
     * 枚举
     */
    GROUP_CHANGE("16", "客户群变更消息推送"),
    FRIEND_CHANGE("15", "客户变更消息推送"),
    UNDEFINED("-1", "未知"),
    ;

    private final String code;

    private final String name;

    public static WsCallBackTypeEnum parse(String code) {
        if (code != null) {
            for (WsCallBackTypeEnum sourceEnum : WsCallBackTypeEnum.values()) {
                if (sourceEnum.getCode().equals(code)) {
                    return sourceEnum;
                }
            }
        }
        return WsCallBackTypeEnum.UNDEFINED;
    }
}
