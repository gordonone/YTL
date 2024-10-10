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
 * 好友关系表
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_friend_relation")
@ApiModel(value = "WechatFriendRelationEntity对象", description = "好友关系表")
public class WechatFriendRelationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "申请code")
    private String applyCode;

    @ApiModelProperty(value = "客户官方微信userId，即external_userid")
    private String customerWxId;

    @ApiModelProperty(value = "微信uinon_id")
    private String customerWxUnionId;

    @ApiModelProperty(value = "客户微信性别，0-未知 1-男性 2-女性")
    private Integer customerWxGender;

    @ApiModelProperty(value = "客户微信头像")
    private String customerWxAvatar;

    @ApiModelProperty(value = "客户微信昵称")
    private String customerWxName;

    @ApiModelProperty(value = "员工对客户备注姓名")
    private String customerWxRemarkName;

    @ApiModelProperty(value = "员工对客户备注手机号")
    private String customerWxRemarkPhone;

    @ApiModelProperty(value = "员工微信官方userId")
    private String empWxId;

    @ApiModelProperty(value = "好友添加时间")
    private Date addTime;

    @ApiModelProperty(value = "好友关系：0-已添加，1-已删除")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifyTime;


}
