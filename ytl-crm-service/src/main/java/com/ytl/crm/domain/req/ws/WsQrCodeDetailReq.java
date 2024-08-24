package com.ytl.crm.domain.req.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 14:44
 */
@Data
public class WsQrCodeDetailReq {

    /**
     * 活码id
     */
    @JsonProperty("id")
    private String id;

    /**
     * 配置
     */
    @JsonProperty("config_id")
    private String configId;

    /**
     * 回调关联字段
     */
    @JsonProperty("state")
    private String state;
}
