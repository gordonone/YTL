package com.ytl.crm.domain.entity.wechat;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 申请活码记录
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_qrcode_apply_log")
@ApiModel(value = "WechatQrcodeApplyLogEntity对象", description = "申请活码记录")
public class WechatQrcodeApplyLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码，对外提供的applyCode")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "申请类型，渠道码、客户码")
    private String applyType;

    @ApiModelProperty(value = "申请的唯一键")
    private String uniqueKey;

    @ApiModelProperty(value = "渠道code")
    private String channelCode;

    @ApiModelProperty(value = "业务类型，业务自己定")
    private String bizType;

    @ApiModelProperty(value = "业务key，合同号等")
    private String bizKey;

    @ApiModelProperty(value = "业务相关数据，json")
    private String bizParam;

    @ApiModelProperty(value = "客户id类型")
    private String customerIdType;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "员工微信id")
    private String empWxId;

    @ApiModelProperty(value = "员工名称")
    private String empName;

    @ApiModelProperty(value = "找员工的规则，仅适用于客户码")
    private String ruleParam;

    @ApiModelProperty(value = "是否删除 0：否 1：是")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifyTime;

}
