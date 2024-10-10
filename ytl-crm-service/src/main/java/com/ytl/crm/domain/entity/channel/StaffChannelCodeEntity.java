package com.ytl.crm.domain.entity.channel;

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
 *
 * </p>
 *
 * @author hongj
 * @since 2024-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_staff_channel_code")
@ApiModel(value = "StaffChannelCodeEntity对象", description = "")
public class StaffChannelCodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "员工id")
    private Long staffId;

    @ApiModelProperty(value = "申请渠道码唯一标识")
    private Long channelCode;

    @ApiModelProperty(value = "员工企微账号")
    private String externalId;

    @ApiModelProperty(value = "申请末级渠道分类code")
    private String channelCategoryCode;

    @ApiModelProperty(value = "二维码上传地址")
    private String qrCodeUrl;

    @ApiModelProperty(value = "申请码主键")
    private String applyQrCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date lastModifyTime;


}
