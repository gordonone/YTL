package com.ytl.crm.domain.entity.task.config;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ytl.crm.utils.DateTimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_marketing_task_material_cotent")
@ApiModel(value = "MarketingTaskMaterialContentEntity对象", description = "营销企微素材表")
public class MarketingTaskMaterialContentEntity {

    @ApiModelProperty(value = "素材id")
    private String materialId;

    @ApiModelProperty(value = "素材类型/（永久、临时）")
    private String materialType;

    @ApiModelProperty(value = "素材附件类型")
    private String materialAttentionType;

    @ApiModelProperty(value = "素材内容备注")
    private String materialRemark;

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
