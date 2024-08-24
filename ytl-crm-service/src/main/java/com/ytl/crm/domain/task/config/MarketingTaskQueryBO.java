package com.ytl.crm.domain.task.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MarketingTaskQueryBO {


    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    private long currentPage = 1L;
    private long pageSize = 10L;


}
