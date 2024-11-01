package com.ytl.crm.event.wechat.listener.friend;

import com.google.common.eventbus.Subscribe;
import com.ziroom.ugc.crm.service.web.domain.bo.wechat.WechatFriendSaveBO;
import com.ziroom.ugc.crm.service.web.domain.entity.wechat.WechatFriendRelationEntity;
import com.ziroom.ugc.crm.service.web.event.wechat.model.customer.DelFriendEvent;
import com.ziroom.ugc.crm.service.web.event.wechat.model.customer.FriendEventContext;
import com.ziroom.ugc.crm.service.web.logic.interfaces.customer.IUserPlatformAccountLogic;
import com.ziroom.ugc.crm.service.web.logic.interfaces.wechat.IWechatFriendLogic;
import com.ziroom.ugc.crm.service.web.service.interfaces.wechat.IWechatFriendRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelFriendEventListener extends AbstractWeChatFriendListener<DelFriendEvent> {

    private final IWechatFriendRelationService iWechatFriendRelationService;
    private final IWechatFriendLogic iWechatFriendLogic;
    private final IUserPlatformAccountLogic iUserPlatformAccountLogic;

    @Subscribe
    @Override
    public void listen(DelFriendEvent event) {
        super.handleEvent(event);
    }

    @Override
    public FriendEventContext<DelFriendEvent> buildContext(DelFriendEvent event) {
        FriendEventContext<DelFriendEvent> context = FriendEventContext.of(event);
        String customerWxId = event.getCustomerWxId();
        String empWxId = event.getEmpWxId();
        WechatFriendRelationEntity oldRelation = iWechatFriendRelationService.queryByCustomerAndEmpId(customerWxId, empWxId);
        context.setOldFriendRelation(oldRelation);
        return context;
    }

    @Override
    public boolean checkNeedHandle(FriendEventContext<DelFriendEvent> context) {
        if (context.getOldFriendRelation() == null) {
            log.warn("好友关系暂不存在，无需处理");
            return false;
        }
        return true;
    }

    @Override
    public boolean doHandle(FriendEventContext<DelFriendEvent> context) {
        DelFriendEvent event = context.getEvent();
        String empWxId = event.getEmpWxId();
        String customerWxId = event.getCustomerWxId();
        iWechatFriendLogic.delFriend(empWxId, customerWxId);

        //todo 记录数据
        WechatFriendSaveBO wechatFriendSaveBO=new WechatFriendSaveBO();
        wechatFriendSaveBO.setCustomerWxId(customerWxId);
        wechatFriendSaveBO.setEmpWxId(empWxId);
        iUserPlatformAccountLogic.saveUserPlatformAccount(wechatFriendSaveBO);
        return true;
    }
}
