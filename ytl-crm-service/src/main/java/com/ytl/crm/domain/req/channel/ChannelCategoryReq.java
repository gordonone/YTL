package com.ytl.crm.domain.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ChannelCategoryReq", description = "渠道分类请求对象")
public class ChannelCategoryReq extends BaseReq {
    @ApiModelProperty(value = "分类id")
    private Long id;

    @ApiModelProperty(value = "是否禁用  1 禁用  0 启用")
    private Integer disable;

}
