package com.ytl.crm.domain.enums.wechat.official;

import com.ytl.crm.domain.mq.OfficialCallBackType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeChatOfficialEventEnum {
    /**
     * 事件的类型,外部联系人变更
     */
    CHANGE_EXTERNAL_CONTACT(OfficialCallBackType.EVENT_CHANGE_CONTACT, "外部联系人变更");
    private String code;
    private String desc;

    public static WeChatOfficialEventEnum queryByCode(String code) {
        for (WeChatOfficialEventEnum value : WeChatOfficialEventEnum.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

}
