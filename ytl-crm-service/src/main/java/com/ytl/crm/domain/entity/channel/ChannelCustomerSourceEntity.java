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
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_channel_customer_source")
@ApiModel(value = "ChannelCustomerSourceEntity对象", description = "")
public class ChannelCustomerSourceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "渠道id")
    private Long channelInfoId;

    @ApiModelProperty(value = "渠道逻辑code")
    private String channelInfoLogicCode;

    @ApiModelProperty(value = "客户名称")
    private String empName;

    @ApiModelProperty(value = "客户企业微信id")
    private String empWxId;

    @ApiModelProperty(value = "状态   1 启用 2 禁用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;

    @ApiModelProperty(value = "关联渠道的名称")
    private String channelInfoName;


}
