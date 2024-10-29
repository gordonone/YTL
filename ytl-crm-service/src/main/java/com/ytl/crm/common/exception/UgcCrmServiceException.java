package com.ytl.crm.common.exception;


import com.ytl.crm.common.base.UgcCmrServiceRespCodeEnum;
import com.ytl.crm.domain.common.BaseException;


public class UgcCrmServiceException extends BaseException {

    public static final int BIZ_ERROR_CODE = 999000001;

    public UgcCrmServiceException(String message, Throwable cause) {
        super(BIZ_ERROR_CODE, message, cause);
    }

    public UgcCrmServiceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UgcCrmServiceException(int code, String message) {
        super(code, message);
    }

    public UgcCrmServiceException(UgcCmrServiceRespCodeEnum errorEnum) {
        super(errorEnum.getCode(), errorEnum.getMsg());
    }

    public UgcCrmServiceException(String message) {
        super(BIZ_ERROR_CODE, message);
    }

    public UgcCrmServiceException(Throwable cause) {
        super(cause);
    }

}

