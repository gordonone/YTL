package com.ytl.crm.domain.resp.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WechatFriendDetailDTO {

    @ApiModelProperty(value = "客户官方微信userId，即external_userid")
    private String customerWxId;

    @ApiModelProperty(value = "微信union_id")
    private String customerWxUnionId;

    @ApiModelProperty(value = "客户微信性别，0-未知 1-男性 2-女性")
    private Integer customerWxGender;

    @ApiModelProperty(value = "客户微信头像")
    private String customerWxAvatar;

    @ApiModelProperty(value = "客户微信昵称")
    private String customerWxName;

    @ApiModelProperty(value = "员工对客户备注姓名")
    private String customerWxRemarkName;

    @ApiModelProperty(value = "员工对客户备注手机号")
    private String customerWxRemarkPhone;

    @ApiModelProperty(value = "员工微信官方userId")
    private String empWxId;

    @ApiModelProperty(value = "好友添加时间")
    private Date addTime;


}
