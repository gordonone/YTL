package com.ytl.crm.domain.req.channel.dynamic;

import com.ytl.crm.domain.req.channel.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "DynamicSearchReq", description = "动态表单查询请求对象")
public class DynamicSearchReq extends BaseReq {
    @ApiModelProperty(value = "渠道Id")
    private Long channelInfoId;
    @ApiModelProperty(value = "员工名称")
    private String empName;

    @ApiModelProperty(value = "当前页码")
    private Integer curPage=1;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize=10;
}
