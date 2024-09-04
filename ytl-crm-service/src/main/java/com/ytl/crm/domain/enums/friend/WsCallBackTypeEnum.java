package com.ytl.crm.domain.enums.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author lixs5
 * @version 1.0
 * @date 2024/7/22 09:49
 * @since 1.0
 */

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
