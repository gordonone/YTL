package com.ytl.crm.domain.bo.channel;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "StaffAccountSearchBo", description = "")
public class StaffAccountSearchBo implements Serializable {

    @ApiModelProperty(value = "员工名称")
    private String staffName;

}
