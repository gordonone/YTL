package com.ytl.crm.domain.entity.task.config;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 营销任务-动作表
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_marketing_task_action")
@ApiModel(value = "MarketingTaskActionEntity对象", description = "营销任务-动作表")
public class MarketingTaskActionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "任务code，t_c_marketing_task表的logic_code")
    private String taskCode;

    @ApiModelProperty(value = "动作一级类型，见枚举TaskActionOneLevelTypeEnum")
    private String actionOneLevelType;

    @ApiModelProperty(value = "动作二级类型，见枚举TaskActionTwoLevelTypeEnum")
    private String actionTwoLevelType;

    /**
     * {@link com.ziroom.ugc.crm.service.web.domain.enums.task.config.TaskActionDependencyEnum#getCode()}
     */
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

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;


}
