package com.ytl.crm.domain.bo.channel;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "StaffAccountPageBo", description = "")
public class StaffAccountPageBo implements Serializable {

    @ApiModelProperty(value = "员工名称")
    private String staffName;

    @ApiModelProperty(value = "企微账号")
    private String externalId;

    @ApiModelProperty(value = "分页页数")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize = 10;

}
