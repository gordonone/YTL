package com.ytl.crm.domain.resp.ws;

import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 16:58
 */
@Data
public class WsBaseResponse<T> {

    private static final String SUCCESS_CODE = "0";

    private String code;

    private String msg;

    private T data;

    public Boolean isOk() {
        return this.getCode() != null && this.getCode().equals(SUCCESS_CODE);
    }
}
