package com.ytl.crm.domain.resp.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/29 15:30
 */
@Data
public class WorkWechatExternalUserIdResp {

    @ApiModelProperty("返回码")
    @JsonProperty("errcode")
    private Integer errCode;

    @ApiModelProperty("对返回码的文本描述内容")
    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("external_userid")
    private String externalUserId;

    public boolean isOk() {
        return this.errCode == 0;
    }
}
