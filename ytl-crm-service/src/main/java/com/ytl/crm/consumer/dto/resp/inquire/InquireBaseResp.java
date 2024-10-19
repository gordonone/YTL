package com.ytl.crm.consumer.dto.resp.inquire;

import lombok.Data;

@Data
public class InquireBaseResp<T> {
    private static final Integer SUCCESS_CODE = 200;

    private Integer code;
    private String message;
    private T data;

    public boolean isOk() {
        return SUCCESS_CODE.equals(this.code);
    }
}
