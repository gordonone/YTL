package com.ytl.crm.domain.resp.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "渠道信息Vo")
public class ChannelInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "渠道逻辑code")
    private String logicCode;

    @ApiModelProperty(value = "渠道分类code 使用-分割")
    private String categoryCode;

    @ApiModelProperty(value = "渠道名称")
    private String name;

    @ApiModelProperty(value = "状态 0 待启用  1 启用 2 禁用")
    private Integer status;

    @ApiModelProperty(value = "状态显示文本")
    private String statusShowText;

    @ApiModelProperty(value = "渠道分类全路径名称")
    private String categoryFullName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;
}
