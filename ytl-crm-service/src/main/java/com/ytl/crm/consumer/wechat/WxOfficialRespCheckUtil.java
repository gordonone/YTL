package com.ytl.crm.consumer.wechat;

import com.ziroom.ugc.crm.service.common.exception.UgcCrmServiceException;
import com.ziroom.ugc.crm.service.web.consumer.dto.resp.wechat.WeChatBaseResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WxOfficialRespCheckUtil {


    public static <R extends WeChatBaseResp> void checkResp(R resp) {
        if (resp == null) {
            throw new UgcCrmServiceException("调用企微接口失败");
        }
        if (!resp.isSuccess()) {
            log.error("调用企微接口异常，errCode={}，errMsg={}", resp.getErrcode(), resp.getErrmsg());
            if (resp.isTokenErr()) {
                //切面处理重试，如果避免一直重试呢？
                throw new WxOfficialTokenException(resp.getErrmsg());
            } else {
                throw new UgcCrmServiceException("调用企微接口异常，errCode" + resp.getErrcode() + "，errMsg=" + resp.getErrmsg());
            }
        }
    }


}
