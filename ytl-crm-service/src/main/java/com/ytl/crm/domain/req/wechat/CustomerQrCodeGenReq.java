package com.ytl.crm.domain.req.wechat;

import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerQrCodeGenReq extends WechatBaseReq {

    @ApiModelProperty(value = "申请的唯一键，该字段+渠道去重", required = true)
    @NotNull(message = "uniqueKey不能为空")
    private String uniqueKey;

    @ApiModelProperty(value = "渠道code", required = true)
    @NotNull(message = "渠道code不能为空")
    private String channelCode;

    @ApiModelProperty(value = "规则相关数据，用于查找微信号，json", required = true)
    @NotNull(message = "渠道规则值不能为空")
    private Map<String, String> ruleParam;

    @ApiModelProperty(value = "业务类型，业务自己定")
    private String bizType;

    @ApiModelProperty(value = "业务key，合同号等")
    private String bizKey;

    @ApiModelProperty(value = "业务相关数据，json")
    private String bizParam;


    @ApiModelProperty(value = "客户id类型", required = true)
    @NotNull(message = "客户id类型")
    private String customerIdType;

    @ApiModelProperty(value = "客户id值", required = true)
    @NotNull(message = "客户id值")
    private String customerId;

    @ApiModelProperty(value = "二维码来源，如不传，默认OFFICIAL-企微官方")
    private String qrCodeSource = WechatSourceEnum.OFFICIAL.getCode();

}
