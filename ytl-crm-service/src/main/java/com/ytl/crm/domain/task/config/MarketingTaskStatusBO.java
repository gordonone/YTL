package com.ytl.crm.domain.task.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MarketingTaskStatusBO {

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "任务状态，见枚举TaskStatusEnum")
    private String taskStatus;
}
