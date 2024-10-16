package com.ytl.crm.domain.resp.channel;

import com.ytl.crm.domain.resp.common.CommonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BaseResp", description = "基础返回对象")
public class BaseResp implements Serializable {
    @ApiModelProperty(value = "字典数据")
    private List<CommonDict> dictList;
}
