package com.ytl.crm.domain.entity.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ytl.crm.constants.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author hongj
 * @since 2024-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_c_staff_platform_account")
@ApiModel(value = "StaffPlatformAccountEntity", description = "")
public class StaffPlatformAccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "企微id，账号前缀")
    private String externalId;

    @ApiModelProperty(value = "员工账号类型")
    private Integer accountType;

    @ApiModelProperty(value = "员工名称")
    private String staffName;

    @ApiModelProperty(value = "员工电话")
    private String staffPhone;

    @ApiModelProperty(value = "员工岗位名称")
    private String position;

    @ApiModelProperty(value = "员工类型")
    private String staffType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "修改人编号")
    private String modifyUserCode = CommonConstant.SYSTEM;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName = CommonConstant.SYSTEM;


}
