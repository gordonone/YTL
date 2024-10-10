package com.ytl.crm.domain.bo.task.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MarketingTaskStatusBO {

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "任务状态，见枚举TaskStatusEnum")
    private String taskStatus;


    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;
}
