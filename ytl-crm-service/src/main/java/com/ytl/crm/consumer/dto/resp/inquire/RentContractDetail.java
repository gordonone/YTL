package com.ytl.crm.consumer.dto.resp.inquire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RentContractDetail {

    @ApiModelProperty(value = "合同号")
    private String contractCode;

    @ApiModelProperty(value = "签约日期")
    private String signDate;

    @ApiModelProperty(value = "合同开始时间")
    private String startDate;

    @ApiModelProperty(value = "到期日期")
    private String stopDate;

    @ApiModelProperty(value = "房源编号")
    private String houseSourceCode;

    @ApiModelProperty(value = "合同状态")
    private String contractState;

    @ApiModelProperty(value = "房屋id")
    private String houseId;

    @ApiModelProperty(value = "房源code")
    private String houseCode;

    @ApiModelProperty(value = "物业地址")
    private String address;

    private int houseType;

    @ApiModelProperty(value = "城市编号")
    private String cityCode;

    @ApiModelProperty(value = "uid")
    private String uid;

    @ApiModelProperty(value = "签约电话")
    private String userPhone;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "楼盘id")
    private String villageId;

    @ApiModelProperty(value = "版本id")
    private String ziroomVersionId;

    @ApiModelProperty("收房合同号")
    private String hireContractCode;

}
