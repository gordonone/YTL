package com.ytl.crm.domain.bo.channel;

import com.ziroom.ugc.crm.service.web.domain.constants.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author hongj
 * @since 2024-09-26
 */
@Data
@ApiModel(value = "StaffBaseBo", description = "")
public class StaffBaseBo implements Serializable {


    private Long id;

    @ApiModelProperty(value = "企微账号")
    private String externalId;

    @ApiModelProperty(value = "员工账号类型")
    private Integer accountType;

    @ApiModelProperty(value = "员工名称")
    private String staffName;

    @ApiModelProperty(value = "员工手机号")
    private String staffPhone;

    @ApiModelProperty(value = "员工岗位名称")
    private String position;

    @ApiModelProperty(value = "员工类型")
    private String staffType;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode = CommonConstant.SYSTEM;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName = CommonConstant.SYSTEM;


}
