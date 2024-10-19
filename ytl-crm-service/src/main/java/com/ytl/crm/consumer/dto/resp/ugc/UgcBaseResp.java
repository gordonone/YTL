package com.ytl.crm.consumer.dto.resp.ugc;

import lombok.Data;

@Data
public class UgcBaseResp<T> {

    private static final Integer SUCCESS_CODE = 200;

    private Integer code;
    private String msg;
    private T data;
    private String message;
    private String status;
    private String result;

    public boolean isOk() {
        return SUCCESS_CODE.equals(this.code);
    }

}
