package com.ytl.crm.domain.resp.work;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企微中获取用户信息（包括企微用户、外部联系人即微信用户）
 * 参考文档： <a href="https://developer.work.weixin.qq.com/document/path/91023">获取访问用户身份</a>
 * @author cuiw
 * @version 1.0
 * @date 2024/7/18
 * @since JDK 8.0
 */
@Data
public class WorkWechatUserinfoResp {
    @ApiModelProperty("返回码")
    private int errcode;

    @ApiModelProperty("对返回码的文本描述内容")
    private String errmsg;

    @ApiModelProperty("成员UserID")
    private String userid;

    /**
     * 成员票据，最大为512字节，有效期为1800s。
     * scope为snsapi_privateinfo，且用户在应用可见范围之内时返回此参数。
     */
    @ApiModelProperty("成员票据")
    private String user_ticket;

    /**
     * 非企业成员的标识，对当前企业唯一。不超过64字节
     */
    @ApiModelProperty("非企业成员的标识")
    private String openid;

    /**
     * 外部联系人id，当且仅当用户是企业的客户，且跟进人在应用的可见范围内时返回。
     * 如果是第三方应用调用，针对同一个客户，同一个服务商不同应用获取到的id相同
     */
    @ApiModelProperty("外部联系人id")
    private String external_userid;
}
