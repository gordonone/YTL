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
 * 营销任务-动作执行记录-明细表
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_marketing_task_action_exec_item")
@ApiModel(value = "MarketingTaskActionExecItemEntity对象", description = "营销任务-动作执行记录-明细表")
public class MarketingTaskActionExecItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "任务logicCode")
    private String taskCode;

    @ApiModelProperty(value = "任务触发记录logicCode")
    private String triggerCode;

    @ApiModelProperty(value = "动作配置表logicCode")
    private String actionCode;

    @ApiModelProperty(value = "动作记录code，表t_a_marketing_task_action_exec_record的logicCode")
    private String actionRecordCode;

    @ApiModelProperty(value = "执行参数")
    private String execParam;

    @ApiModelProperty(value = "执行结果，见枚举TaskActionItemExecStatusEnum")
    private String execStatus;

    @ApiModelProperty(value = "执行结果信息")
    private String execMsg;

    @ApiModelProperty(value = "小木管家虚拟号")
    private String virtualKeeperId;

    @ApiModelProperty(value = "小木管家虚拟号-三方id")
    private String virtualKeeperThirdId;

    @ApiModelProperty(value = "小木管家虚拟号-姓名")
    private String virtualKeeperName;

    @ApiModelProperty(value = "三方任务id")
    private String thirdTaskId;

    @ApiModelProperty(value = "三方任务执行状态")
    private String thirdTaskExecStatus;

    @ApiModelProperty(value = "最终执行结果，见枚举TaskActionItemFinalRetEnum")
    private String finalExecRet;

    @ApiModelProperty(value = "回调结果，见枚举")
    private String callBackRet;

    @ApiModelProperty(value = "三方任务创建时间")
    private Date thirdTaskCreateTime;

    @ApiModelProperty(value = "三方任务执行时间")
    private Date thirdTaskExecTime;

    @ApiModelProperty(value = "是否是补偿数据 0:否 1:是")
    private Integer isCompensate;

    @ApiModelProperty(value = "源itemCode，用于任务补偿")
    private String sourceItemCode;

    @ApiModelProperty(value = "是否有效 0:无效  1:有效")
    private Integer isValid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;

}
