package com.ytl.crm.consumer.dto.req.wechat;

import lombok.Data;

@Data
public class WechatDeleteQrCodeReq {
    /**
     * 企业联系方式的配置id
     */
    private String config_id;

}
