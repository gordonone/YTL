package com.ytl.crm.consumer.req.wechat;

import lombok.Data;

import java.util.List;

@Data
public class WechatCreateQrCodeReq {

    /**
     * 联系方式类型,1-单人, 2-多人
     */
    private Integer type;

    /**
     * 场景，1-在小程序中联系，2-通过二维码联系
     */
    private final String scene = "2";

    /**
     * 用于透传业务信息的字段，不超过30个字符
     */
    private String state;

    /**
     * 生成二维码的员工微信id
     * type=1时，只能有一个
     */
    private List<String> user;

}
