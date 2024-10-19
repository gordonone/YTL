package com.ytl.crm.consumer.dto.resp.zoro;

import lombok.Data;

@Data
public class ZoroBaseResp<T> {

    public static final Integer SUCCESS_CODE = 0;

    private Integer status;
    private String message;
    private T data;

    public Boolean isOK() {
        return SUCCESS_CODE.equals(this.status);
    }
}
