package com.ytl.crm.domain.bo.task.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MarketingTaskAddBO {


    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "有效期开始时间,yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTimeStart;

    @ApiModelProperty(value = "有效期结束时间,yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTimeEnd;

    @ApiModelProperty(value = "任务执行开始时间,HH:mm:ss")
    private String actionTimeStartStr;

    @ApiModelProperty(value = "任务执行结束时间,HH:mm:ss")
    private String actionTimeEndStr;


    @ApiModelProperty(value = "触发条件类型，见枚举TaskTriggerTypeEnum")
    private String triggerType;

    @ApiModelProperty(value = "触发条件值，如分群id")
    private String triggerConditionValue;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

}
