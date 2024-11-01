package com.ytl.crm.domain.bo.wechat;

import com.ytl.crm.domain.enums.wechat.QrCodeApplyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerQrCodeApplyBO extends QrCodeApplyBO {

    @ApiModelProperty(value = "业务类型，业务自己定")
    private String bizType;

    @ApiModelProperty(value = "业务key，合同号等")
    private String bizKey;

    @ApiModelProperty(value = "业务相关数据，json")
    private String bizParam;

    @ApiModelProperty(value = "规则相关数据，用于查找微信号")
    private Map<String, String> ruleParam;


    @ApiModelProperty(value = "客户id类型")
    private String customerIdType;

    @ApiModelProperty(value = "客户id值")
    private String customerId;

    public CustomerQrCodeApplyBO(){
        super();
        typeEnum = QrCodeApplyTypeEnum.CUSTOMER;
    }

}
