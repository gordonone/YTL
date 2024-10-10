package com.ytl.crm.domain.bo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "StaffPlatformChannelBo", description = "")
public class StaffPlatformChannelBo implements Serializable {

    @ApiModelProperty(value = "员工id")
    private Long staffId;

    @ApiModelProperty(value = "员工企微账号")
    private String externalId;

    @ApiModelProperty(value = "申请末级渠道分类code")
    private String channelCategoryCode;

    @ApiModelProperty(value = "申请渠道码")
    private String channelName;

    @ApiModelProperty(value = "渠道分类全路径名称")
    private String channelCategoryFullName;

    private String qrCodeUrl;


}
