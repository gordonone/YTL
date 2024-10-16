package com.ytl.crm.domain.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ChannelCategoryAddReq", description = "新增渠道分类请求对象")
public class ChannelCategoryAddReq extends BaseReq {

    @ApiModelProperty(value = "分类名称")
    @NotNull
    private String categoryName;

    @ApiModelProperty(value = "父级id")
    private Long parentId;


    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;
}
