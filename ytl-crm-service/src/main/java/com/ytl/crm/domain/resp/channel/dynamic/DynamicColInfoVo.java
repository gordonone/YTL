package com.ytl.crm.domain.resp.channel.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(value = "动态表头信息")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DynamicColInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "列名称")
    private String colName;

    @ApiModelProperty(value = "列显示名称")
    private String colShowText;

    @ApiModelProperty(value = "1 文本编辑  2 下拉列表单选 3 下拉列表多选 4 radio 5 checkbox")
    private Integer colFormType;

    @ApiModelProperty(value = "列关联的字典类型名称")
    private String colRelateDictName;

    @ApiModelProperty(value = "列数据类型  1 文本 2 数值 3 日期格式")
    private Integer colDataType;

    @JsonIgnore
    private Integer colListShowType;
    @JsonIgnore
    private DynamicColInfoVo child;

}
