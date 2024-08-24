package com.ytl.crm.domain.common;


import com.ytl.crm.domain.enums.BaseResponseCodeEnum;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/3 10:29
 */
public class WechatBusinessException extends BaseException {

    public WechatBusinessException(String message) {
        super(BaseResponseCodeEnum.FAIL.getCode(), message);
    }

    public WechatBusinessException() {
    }
}
