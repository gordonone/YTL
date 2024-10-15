package com.ytl.crm.consumer.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatQrCodeDTO extends WeChatBaseResp {

    /**
     * 配置ID
     */
    @JsonProperty("config_id")
    private String configId;

    /**
     * 二维码地址信息
     */
    @JsonProperty("qr_code")
    private String qrCode;

}
