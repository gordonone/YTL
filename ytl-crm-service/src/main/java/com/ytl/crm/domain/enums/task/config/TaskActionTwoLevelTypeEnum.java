package com.ytl.crm.domain.enums.task.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionTwoLevelTypeEnum {
    /**
     * 群发
     */
    MSG_TO_GROUP_WS("MSG_TO_GROUP_WS", "群发消息to客户群(微盛)", TaskActionOneLevelTypeEnum.BATCH_SEND_MSG),

    /**
     * 创建六类单
     */
    ACTIVE_SERVICE_ORDER("ACTIVE_SERVICE_ORDER", "主动服务单", TaskActionOneLevelTypeEnum.CREATE_SR_ORDER),
    ;

    private final String code;

    private final String desc;

    private final TaskActionOneLevelTypeEnum parentType;

}
