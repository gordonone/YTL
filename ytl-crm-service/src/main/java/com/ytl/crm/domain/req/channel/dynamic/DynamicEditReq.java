package com.ytl.crm.domain.req.channel.dynamic;


import com.ytl.crm.domain.req.channel.BaseReq;
import com.ytl.crm.domain.resp.channel.dynamic.DynamicDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(value = "DynamicEditReq", description = "动态表单编辑请求对象")
public class DynamicEditReq extends BaseReq implements Serializable {
    @ApiModelProperty(value = "渠道名称")
    private String channelInfoName;
    @ApiModelProperty(value = "渠道Id 必传")
    @NotNull
    private Long channelInfoId;
    @ApiModelProperty(value = "渠道LogicCode")
    private String channelInfoLogicCode;
    @ApiModelProperty(value = "员工名称")
    private String empName;
    @ApiModelProperty(value = "员工企业微信id")
    private String empWxId;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "动态列数据")
    private List<DynamicDataVo> dynamicData;
}