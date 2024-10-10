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
 * 渠道相关操作日志表
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_channel_op_log")
@ApiModel(value="ChannelOpLogEntity对象", description="渠道相关操作日志表")
public class ChannelOpLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "业务类型 1. channel  2 other")
    private String businessType;

    @ApiModelProperty(value = "操作数据的逻辑编号")
    private String opDataLogicCode;

    @ApiModelProperty(value = "操作类型  1 新增 2 修改 3 删除 4 禁用 5 启用")
    private Integer opType;

    @ApiModelProperty(value = "修改内容")
    private String content;

    @ApiModelProperty(value = "操作时间")
    private Date createTime;

    @ApiModelProperty(value = "操作人编号")
    private String opUserCode;

    @ApiModelProperty(value = "操作人姓名")
    private String opUserName;

}
