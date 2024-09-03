package com.ytl.crm.domain.entity.task.exec;

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
 * 营销任务-动作执行记录表
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_marketing_task_action_exec_record")
@ApiModel(value = "MarketingTaskActionExecRecordEntity对象", description = "营销任务-动作执行记录表")
public class MarketingTaskActionExecRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "任务code，t_c_marketing_task表的logic_code")
    private String taskCode;

    @ApiModelProperty(value = "触发记录code，t_a_marketing_task_trigger_record表的logic_code")
    private String triggerCode;

    @ApiModelProperty(value = "动作code，t_c_marketing_task_action表的logic_code")
    private String actionCode;

    @ApiModelProperty(value = "动作一级类型，见枚举TaskActionOneLevelTypeEnum")
    private String actionOneLevelType;

    @ApiModelProperty(value = "动作二级类型，见枚举TaskActionTwoLevelTypeEnum")
    private String actionTwoLevelType;

    @ApiModelProperty(value = "执行状态，见枚举TaskActionExecStatusEnum")
    private String actionExecStatus;

    @ApiModelProperty(value = "任务动作执行顺序")
    private Integer actionOrder;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;


}
