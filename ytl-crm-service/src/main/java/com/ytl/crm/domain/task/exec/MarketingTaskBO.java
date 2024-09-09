package com.ytl.crm.domain.task.exec;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class MarketingTaskBO {

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "有效期开始时间")
    private Date validTimeStart;

    @ApiModelProperty(value = "有效期结束时间")
    private Date validTimeEnd;

    @ApiModelProperty(value = "任务执行开始时间")
    private LocalTime actionTimeStart;

    @ApiModelProperty(value = "任务执行结束时间")
    private LocalTime actionTimeEnd;

    @ApiModelProperty(value = "任务状态，见枚举TaskStatusEnum")
    private String taskStatus;

    @ApiModelProperty(value = "触发条件类型，见枚举TaskTriggerTypeEnum")
    private String triggerType;

    @ApiModelProperty(value = "触发条件值，如分群id")
    private String triggerConditionValue;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;



}
