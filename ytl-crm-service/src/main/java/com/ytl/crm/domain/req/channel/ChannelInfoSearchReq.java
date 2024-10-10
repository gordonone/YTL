package com.ytl.crm.domain.req.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ChannelInfoSearchReq", description = "渠道信息搜索参数")
public class ChannelInfoSearchReq extends BaseReq {
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "状态列表")
    private List<Integer> statusList;
    @ApiModelProperty(value = "渠道分类code")
    private String categoryCode;
    @ApiModelProperty(value = "当前页码")
    private Integer curPage=1;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize=10;
}
