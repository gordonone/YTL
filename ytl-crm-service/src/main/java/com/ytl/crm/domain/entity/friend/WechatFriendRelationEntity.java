package com.ytl.crm.domain.entity.friend;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工和客户的微信好友关系表
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_friend_relation")
@ApiModel(value = "WechatFriendRelationEntity对象", description = "员工和客户的微信好友关系表")
public class WechatFriendRelationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "员工id")
    private String virtualEmpId;

    @ApiModelProperty(value = "员工第三方应用id")
    private String virtualEmpThirdId;

    @ApiModelProperty(value = "客户id")
    private String uid;

    @ApiModelProperty(value = "用户备注")
    private String userRemarkName;

    @ApiModelProperty(value = "客户微信id")
    private String userExternalId;

    @ApiModelProperty(value = "客户微信原始id，上面是三方加密的")
    private String userOriginalExternalId;

    @ApiModelProperty(value = "申请渠道标识")
    private String channelCode;

    @ApiModelProperty(value = "好友关系：0-已添加，1-已删除")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;


}
