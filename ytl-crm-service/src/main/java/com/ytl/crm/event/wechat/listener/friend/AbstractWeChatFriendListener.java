package com.ytl.crm.event.wechat.listener.friend;


import com.ytl.crm.domain.enums.wechat.WechatEventStatusEnum;
import com.ytl.crm.event.wechat.listener.IWeChatEventListener;
import com.ytl.crm.event.wechat.model.customer.FriendEvent;
import com.ytl.crm.event.wechat.model.customer.FriendEventContext;
import com.ytl.crm.service.ws.define.wechat.official.IWechatFriendChangeEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public abstract class AbstractWeChatFriendListener<E extends FriendEvent> implements IWeChatEventListener {

    @Resource
    private IWechatFriendChangeEventService iWechatFriendChangeEventService;

    //监听注解需要加到子类，否则会出现重复路由的问题
    public abstract void listen(E event);

    public abstract FriendEventContext<E> buildContext(E event);

    public abstract boolean checkNeedHandle(FriendEventContext<E> context);

    public abstract boolean doHandle(FriendEventContext<E> context);

    public void handleEvent(E event) {
        //1.构建上下文
        String eventEntityLogicCode = event.getFriendEventLogicCode();
        FriendEventContext<E> context = buildContext(event);
        //2.具体判断事件是否需要处理
        boolean needHandle = checkNeedHandle(context);
        if (!needHandle) {
            //修改事件状态为无需处理
            updateEventStatus(eventEntityLogicCode, WechatEventStatusEnum.IGNORE);
            return;
        }

        //3.处理事件
        boolean handleSuccess = false;
        try {
            handleSuccess = doHandle(context);
        } catch (Exception e) {
            log.error("处理企微好友事件异常", e);
        }

        //4.更新事件状态为失败或者成功
        WechatEventStatusEnum targetStatus = handleSuccess ? WechatEventStatusEnum.SUCCESS : WechatEventStatusEnum.FAIL;
        updateEventStatus(eventEntityLogicCode, targetStatus);

    }

    private void updateEventStatus(String logicCode, WechatEventStatusEnum toStatus) {
        boolean updateRet = iWechatFriendChangeEventService.updateEventStatus(logicCode,
                WechatEventStatusEnum.INIT.getCode(), toStatus.getCode());
        log.info("更新事件状态，logicCode={}，toStatus={}，updateRet={}", logicCode, toStatus.getCode(), updateRet);
    }


}
