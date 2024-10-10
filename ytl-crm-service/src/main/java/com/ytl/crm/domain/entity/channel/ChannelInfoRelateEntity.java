package com.ytl.crm.domain.entity.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 渠道信息关联表（渠道分配策略和客源分配规则关联）
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_channel_info_relate")
@ApiModel(value="ChannelInfoRelateEntity对象", description="渠道信息关联表（渠道分配策略和客源分配规则关联）")
public class ChannelInfoRelateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联的渠道信息id")
    private Long channelId;

    @ApiModelProperty(value = "关联类型 1. 渠道分配策略 2. 客源分配规则")
    private Integer type;

    @ApiModelProperty(value = "关联对象的id  type=1 关联渠道分配策略的id   type=2 关联客源分配规则表（动态表头）的id")
    private Long relateId;


}
