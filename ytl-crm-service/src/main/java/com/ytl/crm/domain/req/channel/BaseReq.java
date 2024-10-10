package com.ytl.crm.domain.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "BaseReq", description = "请求基类")
@Data
public class BaseReq implements Serializable {
    @ApiModelProperty(value = "用户id")
    private String uid;
    @ApiModelProperty(value = "用户名")
    private String userName;
}
