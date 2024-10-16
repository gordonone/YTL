package com.ytl.crm.domain.resp.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ApiModel(value = "通用字典对象")
public class CommonDict<TK, TV> implements Serializable {

    @ApiModelProperty(value = "字典类型名称")
    private String dictType;
    @ApiModelProperty(value = "字典列表")
    private List<DictNodeVo<TK,TV>> nodes;
    public CommonDict(String dictType)
    {
        this.dictType=dictType;
        nodes =new ArrayList<>();
    }


    public DictNodeVo<TK,TV> put(TK key, TV value) {
        DictNodeVo<TK, TV> tktvDictNodeVo = new DictNodeVo<>(key, value);
        nodes.add(tktvDictNodeVo);
        return tktvDictNodeVo;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DictNodeVo<TK,TV> implements Serializable{
        private TK key;
        private TV value;
    }
}
