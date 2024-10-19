package com.ytl.crm.consumer.dto.resp.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonResult<T> {
    private int code;
    private String message;
    private T result;


    public static <T> JsonResult<T> ok() {
        JsonResult<T> result = new JsonResult<>();
        result.setMessage("success");
        result.setCode(200);
        return result;
    }

    public static <T> JsonResult<T> ok(T resultObj) {
        JsonResult<T> result = new JsonResult<>();
        result.setResult(resultObj);
        result.setMessage("success");
        result.setCode(200);
        return result;
    }

    public static <T> JsonResult<T> error(int errCode, String message) {
        JsonResult<T> result = new JsonResult<>();
        result.setCode(errCode);
        result.setMessage(message);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "JsonResult{" + "code=" + code + ", message='" + message + '\'' + ", result=" + result + '}';
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == 200;
    }
}
