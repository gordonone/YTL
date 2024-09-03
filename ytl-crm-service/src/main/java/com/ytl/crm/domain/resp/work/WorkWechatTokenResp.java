package com.ytl.crm.domain.resp.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ytl.crm.domain.constant.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/29 16:25
 */
@Data
public class WorkWechatTokenResp {

    @ApiModelProperty("返回码")
    @JsonProperty("errcode")
    private Integer errCode;

    @ApiModelProperty("对返回码的文本描述内容")
    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * 创建时间戳 毫秒
     */
    private Long createAt;

    public boolean isOk() {
        return Constants.WORK_WECHAT_SUCCESS_CODE.equals(this.errCode);
    }
}
