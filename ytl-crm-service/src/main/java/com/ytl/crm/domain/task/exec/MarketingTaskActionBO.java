package com.ytl.crm.domain.task.exec;


import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MarketingTaskActionBO {

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "任务code，t_c_marketing_task表的logic_code")
    private String taskCode;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    /**
     * 动作素材，仅发消息有
     */
    private List<MarketingTaskActionMaterialBO> materialList;

    public static MarketingTaskActionBO assemblyBO(MarketingTaskActionEntity actionEntity,
                                                   List<MarketingTaskActionMaterialEntity> materialEntityList) {
        MarketingTaskActionBO actionBO = new MarketingTaskActionBO();
        BeanUtils.copyProperties(actionEntity, actionBO);
        // 动作素材
        List<MarketingTaskActionMaterialBO> actionMaterialBOList = !CollectionUtils.isEmpty(materialEntityList) ?
                materialEntityList.stream().map(MarketingTaskActionMaterialBO::assemblyBO).collect(Collectors.toList())
                : Collections.emptyList();

        actionBO.setMaterialList(actionMaterialBOList);
        return actionBO;
    }


}
