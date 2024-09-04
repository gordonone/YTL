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
 * 营销任务-关联业务数据
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_marketing_task_biz_info")
@ApiModel(value = "MarketingTaskBizInfoEntity对象", description = "营销任务-关联业务数据")
public class MarketingTaskBizInfoEntity implements Serializable {

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
    private String triggerRecordCode;

    @ApiModelProperty(value = "业务类型，见枚举TaskRelatedBizInfoTypeEnum")
    private String bizType;

    @ApiModelProperty(value = "业务号，合同号")
    private String bizCode;

    @ApiModelProperty(value = "用户uid")
    private String customerId;

    @ApiModelProperty(value = "用户三方id-微盛")
    private String customerThirdId;

    @ApiModelProperty(value = "用户姓名")
    private String customerName;

    @ApiModelProperty(value = "群code")
    private String groupCode;

    @ApiModelProperty(value = "群三方code-微盛")
    private String groupThirdCode;

    @ApiModelProperty(value = "群名称")
    private String groupName;

    @ApiModelProperty(value = "虚拟员工虚拟号")
    private String virtualKeeperId;

    @ApiModelProperty(value = "虚拟员工虚拟号-三方id-微盛")
    private String virtualKeeperThirdId;

    @ApiModelProperty(value = "虚拟员工虚拟号-姓名")
    private String virtualKeeperName;

    @ApiModelProperty(value = "是否有效，0-无效，1有效")
    private Integer isValid;

    @ApiModelProperty(value = "无效信息")
    private String notValidMsg;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;

}
