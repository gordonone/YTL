package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThirdCustomerIdConvertResp extends WeChatBaseResp {

    @JsonProperty("external_userid")
    private String externalUserid;

}
