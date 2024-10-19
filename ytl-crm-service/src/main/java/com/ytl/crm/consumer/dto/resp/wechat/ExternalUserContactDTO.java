package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外部联系人详情
 * https://work.weixin.qq.com/api/doc/90000/90135/92114
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalUserContactDTO {
    @JsonProperty(value = "external_userid")
    private String externalUserId;

    private String name;
    private String position;
    private String avatar;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("corp_full_name")
    private String corpFullName;
    private Integer type;
    private Integer gender;
    @JsonProperty("unionid")
    private String unionId;
    @JsonProperty("external_profile")
    private ExternalUserProfileDTO externalProfile;

}       
