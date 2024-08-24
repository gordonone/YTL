package com.ytl.crm.domain.common;

import com.ytl.crm.domain.enums.BaseResponseCodeEnum;
import org.apache.commons.lang3.StringUtils;

public class BaseResponse<T> {
    private Integer code;
    private String msg;
    private T data;
    private String message;
    private String status;
    private String result;

    public BaseResponse() {
        this.code = BaseResponseCodeEnum.OK.getCode();
        this.status = "fail";
        this.result = "false";
    }

    public BaseResponse(Integer code, String msg) {
        this.code = BaseResponseCodeEnum.OK.getCode();
        this.status = "fail";
        this.result = "false";
        this.code = code;
        this.msg = msg;
        this.message = msg;
        if (this.isOK()) {
            this.status = "success";
            this.result = "true";
        } else {
            this.status = "fail";
            this.result = "false";
        }

    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = BaseResponseCodeEnum.OK.getCode();
        this.status = "fail";
        this.result = "false";
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.message = msg;
        if (this.isOK()) {
            this.status = "success";
            this.result = "true";
        } else {
            this.status = "fail";
            this.result = "false";
        }

    }

    public void setMsg(String msg) {
        this.msg = msg;
        this.message = msg;
    }

    public String getMessage() {
        return StringUtils.isNotBlank(this.message) ? this.msg : this.message;
    }

    public Boolean isOK() {
        return this.code == null ? false : this.code.equals(BaseResponseCodeEnum.OK.getCode());
    }

    public static <T> BaseResponse<T> responseOk() {
        BaseResponse<T> baseResponseBo = new BaseResponse(BaseResponseCodeEnum.OK.getCode(), BaseResponseCodeEnum.OK.getMsg());
        return baseResponseBo;
    }

    public static <T> BaseResponse<T> responseOkMsg(String msg) {
        BaseResponse<T> baseResponseBo = new BaseResponse(BaseResponseCodeEnum.OK.getCode(), msg);
        return baseResponseBo;
    }

    public static <T> BaseResponse<T> responseOk(T obj) {
        BaseResponse<T> baseResponseBo = new BaseResponse(BaseResponseCodeEnum.OK.getCode(), BaseResponseCodeEnum.OK.getMsg(), obj);
        return baseResponseBo;
    }

    public static <T> BaseResponse<T> responseFail() {
        BaseResponse<T> baseResponseBo = new BaseResponse(BaseResponseCodeEnum.FAIL.getCode(), BaseResponseCodeEnum.FAIL.getMsg());
        return baseResponseBo;
    }

    public static <T> BaseResponse<T> responseFail(String msg) {
        BaseResponse<T> baseResponseBo = new BaseResponse(BaseResponseCodeEnum.FAIL.getCode(), msg);
        return baseResponseBo;
    }

    public static <T> BaseResponse<T> responseFail(Integer code, String msg) {
        BaseResponse<T> baseResponseBo = new BaseResponse(code, msg);
        return baseResponseBo;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public String getStatus() {
        return this.status;
    }

    public String getResult() {
        return this.result;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String toString() {
        return "BaseResponse(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ", message=" + this.getMessage() + ", status=" + this.getStatus() + ", result=" + this.getResult() + ")";
    }
}

