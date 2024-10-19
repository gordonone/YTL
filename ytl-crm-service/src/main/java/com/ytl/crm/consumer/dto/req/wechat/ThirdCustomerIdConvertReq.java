package com.ytl.crm.consumer.dto.req.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ThirdCustomerIdConvertReq {

    @JsonProperty("external_userid")
    private String externalUserId;

    @JsonProperty("source_agentid")
    private String sourceAgentId;

}
