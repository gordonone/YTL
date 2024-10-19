package com.ytl.crm.consumer.dto.resp.ws;

import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 16:58
 */
@Data
public class WsBaseResponse<T> {

    private static final Integer SUCCESS_CODE = 0;

    private Integer code;

    private String msg;

    private T data;

    public Boolean isOk() {
        return SUCCESS_CODE.equals(code);
    }
}
