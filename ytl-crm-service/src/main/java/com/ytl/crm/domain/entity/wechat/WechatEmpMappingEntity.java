package com.ytl.crm.domain.entity.wechat;

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
 * 员工微信映射表
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_emp_mapping")
@ApiModel(value = "WechatEmpMappingEntity对象", description = "员工微信映射表")
public class WechatEmpMappingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "官方微信id")
    private String empWxId;

    @ApiModelProperty(value = "第三方微信id")
    private String empThirdWxId;

    @ApiModelProperty(value = "三方来源")
    private String thirdSource;

    @ApiModelProperty(value = "是否删除 0:未删除  1:已删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifyTime;


}
