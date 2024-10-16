package com.ytl.crm.api.callback.wechat;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 暂时不需要，使用ugc-wechat的mq
 */
@Deprecated
@Slf4j
@RestController
@Api(value = "微盛回调", tags = "微盛回调")
@RequestMapping("/callback/wechat/ws")
@RequiredArgsConstructor
public class WsCallbackApi {


    /**
     * 好友关系事件回调
     */
    @PostMapping("/notify")
    public String notify(@RequestBody String json) {
        try {

        } catch (Exception e) {
            log.error("微盛回调事件处理异常，异常原因", e);
            return "FAIL";
        }
        return "SUCCESS";
    }
}
