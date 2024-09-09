package com.ytl.crm.domain.entity.friend;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 申请活码记录
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_apply_qrcode_log")
public class WechatApplyQrcodeLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "虚拟管家id")
    private String virtualEmpId;

    @ApiModelProperty(value = "虚拟管家姓名")
    private String virtualEmpName;

    @ApiModelProperty(value = "虚拟管家头图地址")
    private String virtualEmpAvatar;

    @ApiModelProperty(value = "虚拟管家第三方应用id")
    private String virtualEmpThirdId;

    @ApiModelProperty(value = "用户标识")
    private String uid;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户备注姓名")
    private String userRemarkName;

    @ApiModelProperty(value = "申请渠道标识")
    private String channelCode;

    @ApiModelProperty(value = "活码id")
    private String qrcodeId;

    @ApiModelProperty(value = "活码地址")
    private String qrcodeUrl;

    @ApiModelProperty(value = "活码state，用于关联")
    private String state;

    @ApiModelProperty(value = "是否删除 0：否    1：是")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;


}
