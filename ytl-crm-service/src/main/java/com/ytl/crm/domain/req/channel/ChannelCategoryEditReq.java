package com.ytl.crm.domain.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ChannelCategoryEditReq", description = "渠道分类编辑请求对象")
public class ChannelCategoryEditReq extends BaseReq {

    @ApiModelProperty(value = "分类id")
    @NotNull
    private Long id;
    @ApiModelProperty(value = "分类名称")
    @NotEmpty
    private String categoryName;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;
}
