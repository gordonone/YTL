package com.ytl.crm.consumer.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExternalContractListResp {

    private Integer errcode;
    private String errmsg;

    @JsonProperty("external_userid")
    private List<String> externalUserId;
}
