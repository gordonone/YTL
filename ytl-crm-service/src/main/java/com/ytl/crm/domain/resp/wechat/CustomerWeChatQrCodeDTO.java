package com.ytl.crm.domain.resp.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWeChatQrCodeDTO {

    @ApiModelProperty(value = "申请code，可通过applyCode再查询")
    private String applyCode;

    @ApiModelProperty(value = "二维码链接")
    private String qrCodeUrl;

    @ApiModelProperty(value = "二维码渠道来源，目前仅支持企微官方")
    private String qrCodeSource;

    @ApiModelProperty(value = "员工企业微信id")
    private String empWxId;

    @ApiModelProperty(value = "员工名称")
    private String empName;


}
