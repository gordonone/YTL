package com.ytl.crm.domain.task.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MarketingTaskConfigAddBO {

    /**
     * 任务基本信息
     */
    @ApiModelProperty(value = "任务基本信息")
    private MarketingTaskAddBO taskBaseInfo;

    /**
     * 任务动作信息，按执行顺序排序
     */
    @ApiModelProperty(value = "任务动作信息")
    private List<MarketingTaskActionAddBO> taskActionList;




}
