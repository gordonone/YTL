package com.ytl.crm.consumer.dto.resp.data;

import lombok.Data;

@Data
public class DataServiceBaseResp<T> {

    private static final String SUCCESS_CODE = "0";

    private String code;

    private T data;

    private String message;

    private String messageDetail;

    private Integer total;

    public boolean isOk() {
        return SUCCESS_CODE.equalsIgnoreCase(code);
    }

}
