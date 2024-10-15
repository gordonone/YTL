package com.ytl.crm.consumer.req.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ThirdEmpIdConvertReq {

    @JsonProperty("open_userid_list")
    private List<String> openUserIdList;

    @JsonProperty("source_agentid")
    private String sourceAgentId;

}
