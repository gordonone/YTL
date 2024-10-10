package com.ytl.crm.domain.bo.task.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class MarketingTaskQueryBO {


    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    @ApiModelProperty(value = "有效期开始时间,yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTimeStart;

    @ApiModelProperty(value = "有效期结束时间,yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTimeEnd;

    @ApiModelProperty(value = "分页页数")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize = 10;


}
