package com.ytl.crm.consumer.wechat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WechatMediaHelper {

    private final WxOfficialTokenHelper wxOfficialTokenHelper;


    public String getTemporaryMediaId(String mediaId) {

        String accessToken = wxOfficialTokenHelper.acquireAccessToken();



        return null;
    }




}
