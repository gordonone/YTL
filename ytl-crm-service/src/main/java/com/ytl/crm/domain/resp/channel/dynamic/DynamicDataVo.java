package com.ytl.crm.domain.resp.channel.dynamic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DynamicDataVo implements Serializable {

    @ApiModelProperty(value = "关联的数据id")
    private Long dataId;

    @ApiModelProperty(value = "列名")
    private String colName;

    @ApiModelProperty(value = "列值")
    private String colValue;

    @ApiModelProperty(value = "列值展示文本")
    private String colShowText;

}
