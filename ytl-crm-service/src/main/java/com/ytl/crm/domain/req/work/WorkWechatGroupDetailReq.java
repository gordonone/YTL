package com.ytl.crm.domain.req.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/31 10:37
 */
@Data
public class WorkWechatGroupDetailReq {

    @JsonProperty("chat_id")
    private String chatId;
}
