package com.ytl.crm.domain.bo.wechat;

import com.ziroom.ugc.crm.service.web.domain.enums.wechat.QrCodeApplyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeApplyBO {

    @ApiModelProperty(value = "申请的唯一键")
    private String uniqueKey;

    @ApiModelProperty(value = "渠道code")
    private String channelCode;

    @ApiModelProperty(value = "申请类型")
    protected QrCodeApplyTypeEnum typeEnum;

    @ApiModelProperty(value = "员工微信id")
    private String empWxId;

    @ApiModelProperty(value = "员工名称")
    private String empName;

}
