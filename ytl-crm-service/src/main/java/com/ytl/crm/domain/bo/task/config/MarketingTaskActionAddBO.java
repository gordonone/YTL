package com.ytl.crm.domain.bo.task.config;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MarketingTaskActionAddBO {


    @ApiModelProperty(value = "动作一级类型，见枚举TaskActionOneLevelTypeEnum")
    private String actionOneLevelType;

    @ApiModelProperty(value = "动作二级类型，见枚举TaskActionTwoLevelTypeEnum")
    private String actionTwoLevelType;

    @ApiModelProperty(value = "动作依赖关系，见枚举TaskActionDependencyEnum")
    private String actionDependency;

    @ApiModelProperty(value = "动作顺序")
    private Integer actionOrder;

    @ApiModelProperty(value = "首日未执行成功，次日是否继续（0：否 1：是）")
    private Integer isTomorrowContinue;

    /**
     * 动作素材，仅发消息有
     */
    private List<MarketingTaskActionMaterialAddBO> materialList;



}
