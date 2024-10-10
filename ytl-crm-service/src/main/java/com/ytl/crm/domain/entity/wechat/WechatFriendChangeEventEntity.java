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
 * 好友相关事件
 * </p>
 *
 * @author hongj
 * @since 2024-10-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_a_wechat_friend_change_event")
@ApiModel(value = "WechatFriendChangeEventEntity对象", description = "好友相关事件")
public class WechatFriendChangeEventEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，拼接后用于与企业微信交互")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "逻辑编码")
    @TableField(fill = FieldFill.INSERT)
    private String logicCode;

    @ApiModelProperty(value = "traceId，串联事件")
    private String traceId;

    @ApiModelProperty(value = "事件类型")
    private String eventType;

    @ApiModelProperty(value = "事件处理状态")
    private String eventStatus;

    @ApiModelProperty(value = "客户官方微信userId，即external_userid")
    private String customerWxId;

    @ApiModelProperty(value = "员工微信官方userId")
    private String empWxId;

    @ApiModelProperty(value = "事件来源")
    private String eventSource;

    @ApiModelProperty(value = "原始消息")
    private String originMsg;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifyTime;


}
