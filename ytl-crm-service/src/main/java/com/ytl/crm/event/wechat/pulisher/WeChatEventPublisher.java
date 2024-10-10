package com.ytl.crm.event.wechat.pulisher;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import com.ytl.crm.event.wechat.model.WeChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatEventPublisher {
    @Resource(name = "weChatEventBus")
    private EventBus eventBus;

    public void postWeChatEvent(WeChatEvent event) {
        eventBus.post(event);
        log.info("企微事件发布情况：event={}", JSON.toJSONString(event));
    }
}
