package com.ytl.crm.domain.req.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/29 15:34
 */
@Data
public class ConvertExternalUserIdReq {

    @JsonProperty("external_userid")
    private String externalUserid;
    @JsonProperty("source_agentid")
    private Integer sourceAgentId;
}
