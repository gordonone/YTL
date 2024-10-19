package com.ytl.crm.consumer.dto.resp.wechat;

import lombok.Data;

@Data
public class WeChatBaseResp {

    private static final Integer SUCCESS_CODE = 0;

    /**
     * 出错返回码，为0表示成功，非0表示调用失败
     */
    private Integer errcode;

    /**
     * 返回码提示语
     */
    private String errmsg;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(errcode);
    }


    private static final String ERROR_CODE_40014 = "40014";
    private static final String ERROR_CODE_41001 = "41001";
    private static final String ERROR_CODE_42001 = "42001";

    /**
     * 如果微信服务调用返回码为
     * 40014[不合法的access_token]
     * 41001[缺少access_token参数]
     * 42001[access_token已过期]
     * 则重新获取授权码，重试一次
     *
     * @return boolean 是否重试
     */
    public boolean isTokenErr() {
        String errorCode = String.valueOf(this.errcode);
        boolean needRetry = false;
        if (ERROR_CODE_40014.equals(errorCode)) {
            needRetry = true;
        }

        if (ERROR_CODE_41001.equals(errorCode)) {
            needRetry = true;
        }

        if (ERROR_CODE_42001.equals(errorCode)) {
            needRetry = true;
        }
        return needRetry;
    }

}
