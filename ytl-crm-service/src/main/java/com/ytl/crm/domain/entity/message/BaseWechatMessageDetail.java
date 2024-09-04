package com.ytl.crm.domain.entity.message;

import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/1 15:12
 */
@Data
public abstract class BaseWechatMessageDetail {

    private String messageType;

    public BaseWechatMessageDetail(String messageType) {
        this.messageType = messageType;
    }
}
