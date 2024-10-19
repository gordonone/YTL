package com.ytl.crm.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.ytl.crm.event.wechat.listener.IWeChatEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class EventBusConfig {

    @Resource
    private List<IWeChatEventListener> weChatEventListenerList;

    @Bean("weChatEventBus")
    public EventBus eventBus() {
        EventBus eventBus = new AsyncEventBus("weChatEventBus", threadPoolExecutor());
        if (!CollectionUtils.isEmpty(weChatEventListenerList)) {
            for (IWeChatEventListener listener : weChatEventListenerList) {
                eventBus.register(listener);
            }
        }
        return eventBus;
    }

    @Bean("weChatEventBusThreadPool")
    public ThreadPoolExecutor threadPoolExecutor() {

        return new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));
    }

}
