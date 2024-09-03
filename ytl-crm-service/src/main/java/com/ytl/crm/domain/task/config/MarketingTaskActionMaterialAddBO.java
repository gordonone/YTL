package com.ytl.crm.domain.task.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MarketingTaskActionMaterialAddBO {


    @ApiModelProperty(value = "素材类型，见枚举")
    private String materialType;

    @ApiModelProperty(value = "素材内容")
    private String materialContent;

    @ApiModelProperty(value = "素材id")
    private String materialId;

    @ApiModelProperty(value = "素材排序")
    private Integer materialOrder;

    @ApiModelProperty(value = "发送方式，见枚举")
    private String sendType;

}
