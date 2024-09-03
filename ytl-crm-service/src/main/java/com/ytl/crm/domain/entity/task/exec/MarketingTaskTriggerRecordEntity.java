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
 * 营销任务-触发记录表
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_marketing_task_trigger_record")
@ApiModel(value = "MarketingTaskTriggerRecordEntity对象", description = "营销任务-触发记录表")
public class MarketingTaskTriggerRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "任务code，t_c_marketing_task表的logic_code")
    private String taskCode;


    @ApiModelProperty(value = "触发状态，见枚举TaskTriggerStatusEnum")
    private String triggerStatus;

    @ApiModelProperty(value = "触发描述，用于记录失败原因等")
    private String triggerDesc;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;

}
