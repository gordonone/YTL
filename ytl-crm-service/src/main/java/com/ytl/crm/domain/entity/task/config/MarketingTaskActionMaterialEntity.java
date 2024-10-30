package com.ytl.crm.domain.entity.task.config;

import com.baomidou.mybatisplus.annotation.*;
import com.ytl.crm.utils.DateTimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 营销任务-动作素材表
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_marketing_task_action_material")
@ApiModel(value = "MarketingTaskActionMaterialEntity对象", description = "营销任务-动作素材表")
public class MarketingTaskActionMaterialEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "任务code，t_c_marketing_task表的logic_code")
    private String taskCode;

    @ApiModelProperty(value = "动作code，t_c_marketing_task_action表的logic_code")
    private String actionCode;

    @ApiModelProperty(value = "素材附件类型，见枚举")
    private String materialType;

    @ApiModelProperty(value = "素材内容")
    private String materialContent;

    @ApiModelProperty(value = "素材附件内容备注")
    private String materialRemark;

    @ApiModelProperty(value = "素材id")
    private String materialId;

    @ApiModelProperty(value = "素材排序")
    private Integer materialOrder;

    @Deprecated
    @ApiModelProperty(value = "发送方式，见枚举")
    private String sendType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime = DateTimeUtil.currentTime();

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;


}
