package com.ytl.crm.domain.entity.task.config;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    @ApiModelProperty(value = "素材附件图片链接")
    private String materialAttentionUrl;

    @ApiModelProperty(value = "素材附件媒体id")
    private String materialAttentionMediaId;

    @ApiModelProperty(value = "素材内容备注")
    private String materialRemark;
}
