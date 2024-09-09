package com.ytl.crm.domain.entity.task.config;

import com.baomidou.mybatisplus.annotation.*;
import com.ytl.crm.utils.DateTimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

/**
 * <p>
 * 营销任务表
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_marketing_task")
@ApiModel(value = "MarketingTaskEntity对象", description = "营销任务表")
public class MarketingTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
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

    private String taskStatus;

    private String triggerType;

    @ApiModelProperty(value = "上次触发时间，用于查找可执行的任务")
    private Date lastTriggerTime;

    @ApiModelProperty(value = "触发条件值，如分群id")
    private String triggerConditionValue;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifyTime= DateTimeUtil.currentTime();

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;


}
