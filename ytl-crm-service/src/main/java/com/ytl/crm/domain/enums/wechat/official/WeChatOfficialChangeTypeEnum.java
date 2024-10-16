package com.ytl.crm.domain.enums.wechat.official;

import com.ytl.crm.domain.mq.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ytl.crm.domain.enums.wechat.official.WeChatOfficialEventEnum.CHANGE_EXTERNAL_CONTACT;


@Getter
@AllArgsConstructor
public enum WeChatOfficialChangeTypeEnum {
    ADD_EXTERNAL_CONTACT(CHANGE_EXTERNAL_CONTACT, OfficialCallBackType.CHANGE_TYPE_CONTACT_ADD,
            "添加外部联系人", AddExternalContactMsgData.class),
    DEL_EXTERNAL_CONTACT(CHANGE_EXTERNAL_CONTACT, OfficialCallBackType.CHANGE_TYPE_CONTACT_DEL,
            "删除外部联系人", DelExternalContactMsgData.class),
    EDIT_EXTERNAL_CONTACT(CHANGE_EXTERNAL_CONTACT, OfficialCallBackType.CHANGE_TYPE_CONTACT_EDIT,
            "编辑外部联系人", EditExternalContactMsgData.class),
    DEL_FOLLOW_USER(CHANGE_EXTERNAL_CONTACT, OfficialCallBackType.CHANGE_TYPE_FOLLOW_USER_DEL,
            "被外部联系人删除", DelFollowUserMsgData.class),
    ;
    private WeChatOfficialEventEnum eventType;
    private String code;
    private String desc;
    private Class<? extends WeChatEventMsgData> dataClazz;

    public static WeChatOfficialChangeTypeEnum queryChangeType(WeChatOfficialEventEnum eventType, String changeType) {
        for (WeChatOfficialChangeTypeEnum value : WeChatOfficialChangeTypeEnum.values()) {
            if (value.eventType.equals(eventType) && value.code.equalsIgnoreCase(changeType)) {
                return value;
            }
        }
        return null;
    }


}
