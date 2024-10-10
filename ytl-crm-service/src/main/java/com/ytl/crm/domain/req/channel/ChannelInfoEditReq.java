package com.ytl.crm.domain.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ChannelInfoEditReq", description = "渠道信息编辑请求对象")
public class ChannelInfoEditReq extends BaseReq {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "渠道分类code")
    private String categoryCode;

    @ApiModelProperty(value = "分类短码全路径 使用-分割")
    private String categoryShortCodes;

    @ApiModelProperty(value = "渠道名称")
    private String name;

    @ApiModelProperty(value = "状态 0 待启用  1 启用 2 禁用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "规则id集合")
    private List<Long> ruleIds;
}
