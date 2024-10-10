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
 * 活码记录
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_qrcode")
@ApiModel(value = "WechatQrcodeEntity对象", description = "活码记录")
public class WechatQrcodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "申请code，t_a_wechat_apply_qrcode_log表logic_code")
    private String applyCode;


    @ApiModelProperty(value = "二维码来源，官方，供应商")
    private String source;

    @ApiModelProperty(value = "供应商state，微盛用的自己的state")
    private String sourceState;

    @ApiModelProperty(value = "活码id")
    private String qrcodeId;

    @ApiModelProperty(value = "活码地址")
    private String qrcodeUrl;

    /**
     * 过期时间，30天，定期删除
     */
    @ApiModelProperty(value = "活码过期时间")
    private Date qrcodeExpireTime;

    @ApiModelProperty(value = "是否删除 0：否 1：是")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;


}
