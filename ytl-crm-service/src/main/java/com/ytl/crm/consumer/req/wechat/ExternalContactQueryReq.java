package com.ytl.crm.consumer.req.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalContactQueryReq {

    /**
     * 外部联系人的userid
     */
    @JsonProperty("external_userid")
    private String externalUserId;

    /**
     * 上次请求返回的next_cursor，可以为空
     */
    @JsonProperty("cursor")
    private String cursor;

}
