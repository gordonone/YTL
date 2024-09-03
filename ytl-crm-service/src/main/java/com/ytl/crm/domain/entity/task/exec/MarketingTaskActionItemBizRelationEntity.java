package com.ytl.crm.domain.entity.task.exec;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 营销任务-动作执行明细和业务信息关联表
 * </p>
 *
 * @author hongj
 * @since 2024-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_marketing_task_action_item_biz_relation")
@ApiModel(value = "MarketingTaskActionItemBizRelationEntity对象", description = "营销任务-动作执行明细和业务信息关联表")
public class MarketingTaskActionItemBizRelationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "动作执行")
    private String actionRecordCode;

    @ApiModelProperty(value = "动作执行明细code,表t_a_marketing_task_action_exec_item的logicCode")
    private String actionItemCode;

    @ApiModelProperty(value = "任务业务信息code,表t_a_marketing_task_biz_info的logicCode")
    private String taskBizCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;

}
