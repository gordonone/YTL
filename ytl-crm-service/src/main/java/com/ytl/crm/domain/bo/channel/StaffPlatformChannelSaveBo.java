package com.ytl.crm.domain.bo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "StaffPlatformChannelSaveBo", description = "")
public class StaffPlatformChannelSaveBo implements Serializable {

    @ApiModelProperty(value = "员工id")
    private Long staffId;

    @ApiModelProperty(value = "员工企微账号")
    private String externalId;

    @ApiModelProperty(value = "申请渠道码")
    private Long channelCode;


}
