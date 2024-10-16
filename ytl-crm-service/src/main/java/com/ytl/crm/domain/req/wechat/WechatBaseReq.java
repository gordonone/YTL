package com.ytl.crm.domain.req.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 配在apollo，简单校验一下
 */
@Data
public class WechatBaseReq {

    /**
     * 场景
     */
    @ApiModelProperty(value = "接入场景", required = true)
    @NotNull(message = "接入场景不能为空")
    private String scene;

    /**
     * token
     */
    @ApiModelProperty(value = "token", required = true)
    @NotNull(message = "token不能为空")
    private String token;


}
