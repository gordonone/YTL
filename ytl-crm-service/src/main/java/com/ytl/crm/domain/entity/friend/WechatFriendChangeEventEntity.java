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
 * 添加好友事件表
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_friend_change_event")
@ApiModel(value = "WechatFriendChangeEventEntity对象", description = "添加好友事件表")
public class WechatFriendChangeEventEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，拼接后用于与企业微信交互")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "消息标识")
    private String messageId;

    @ApiModelProperty(value = "事件类型，add_external_contact-添加好友，edit_external_contact-编辑好友，del_external_contact-员工删除好友，del_follow_user-客户删除好友")
    private String changeType;

    @ApiModelProperty(value = "小乐管家第三方应用id")
    private String virtualEmpThirdId;

    @ApiModelProperty(value = "外部联系人标识")
    private String externalUserId;

    @ApiModelProperty(value = "自定义字段，添加好友关联")
    private String state;

    @ApiModelProperty(value = "原始事件")
    private String originalEvent;

    @ApiModelProperty(value = "是否删除 0:未删除  1:已删除")
    private Boolean isDelete;

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
