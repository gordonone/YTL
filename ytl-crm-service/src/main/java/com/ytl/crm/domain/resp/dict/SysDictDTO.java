package com.ytl.crm.domain.resp.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysDictDTO {

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "字典code")
    private String code;

    @ApiModelProperty(value = "中文值")
    private String name;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "父级name")
    private String parentCode;

}
