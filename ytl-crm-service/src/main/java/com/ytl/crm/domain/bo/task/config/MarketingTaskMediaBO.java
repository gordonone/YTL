package com.ytl.crm.domain.bo.task.config;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MarketingTaskMediaBO {

    @ApiModelProperty(value = "素材类型，见枚举")
    private String materialType;

    @ApiModelProperty(value = "素材id")
    private String materialId;
}
