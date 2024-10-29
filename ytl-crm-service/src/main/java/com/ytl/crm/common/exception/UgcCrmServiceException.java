package com.ytl.crm.common.exception;


import com.ytl.crm.domain.common.BaseException;


public class UgcCrmServiceException extends BaseException {

    public static int BUSINESS_CODE = 10001;

    public UgcCrmServiceException(String message, Throwable cause) {
        super(BUSINESS_CODE, message, cause);
    }

    public UgcCrmServiceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UgcCrmServiceException(int code, String message) {
        super(code, message);
    }

    public UgcCrmServiceException(String message) {
        super(BUSINESS_CODE, message);
    }

}

