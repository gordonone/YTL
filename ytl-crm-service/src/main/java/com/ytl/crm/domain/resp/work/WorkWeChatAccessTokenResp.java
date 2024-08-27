package com.ytl.crm.domain.resp.work;

import lombok.Data;

/**
 * 从基础平台接口获取accessToken
 *
 * @author cuiw
 * @version 1.0
 * @date 2024/7/19
 * @since JDK 8.0
 */
@Data
public class WorkWeChatAccessTokenResp {

    /**
     * 0 - 表示成功
     */
    private String code;

    /**
     * 返回码说明
     */
    private String message;

    /**
     * 同 code
     * 0 - 表示成功
     */
    private String responseCode;
    private String errorInfo;
    private String messageInfo;
    private String serialNo;

    /**
     * accessToken
     */
    private String data;

    public boolean isOk() {
        return "0".equals(this.code);
    }
}
