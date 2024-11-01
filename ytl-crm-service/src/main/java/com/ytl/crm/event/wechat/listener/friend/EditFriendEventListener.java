package com.ytl.crm.event.wechat.listener.friend;

import com.google.common.eventbus.Subscribe;
import com.ytl.crm.domain.bo.wechat.WechatFriendSaveBO;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.event.wechat.model.customer.EditFriendEvent;
import com.ytl.crm.event.wechat.model.customer.FriendEventContext;
import com.ytl.crm.logic.customer.interfaces.IUserPlatformAccountLogic;
import com.ytl.crm.logic.wechat.interfaces.IWechatFriendLogic;
import com.ytl.crm.service.interfaces.wechat.official.IWechatFriendRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EditFriendEventListener extends AbstractWeChatFriendListener<EditFriendEvent> {

    private final IWechatFriendRelationService iWechatFriendRelationService;
    private final IWechatFriendLogic iWechatFriendLogic;
    private final IUserPlatformAccountLogic iUserPlatformAccountLogic;

    @Subscribe
    @Override
    public void listen(EditFriendEvent event) {
        super.handleEvent(event);
    }

    @Override
    public FriendEventContext<EditFriendEvent> buildContext(EditFriendEvent event) {
        FriendEventContext<EditFriendEvent> context = FriendEventContext.of(event);
        String customerWxId = event.getCustomerWxId();
        String empWxId = event.getEmpWxId();
        WechatFriendRelationEntity oldRelation = iWechatFriendRelationService.queryByCustomerAndEmpId(customerWxId, empWxId);
        context.setOldFriendRelation(oldRelation);
        return context;
    }

    @Override
    public boolean checkNeedHandle(FriendEventContext<EditFriendEvent> context) {
        if (context.getOldFriendRelation() == null) {
            log.warn("好友关系暂不存在，无需处理");
            return false;
        }
        return true;
    }

    @Override
    public boolean doHandle(FriendEventContext<EditFriendEvent> context) {
        //怎么更新
        WechatFriendRelationEntity oldFriendRelation = context.getOldFriendRelation();
        iWechatFriendLogic.updateFriend(oldFriendRelation);

        //todo 记录数据
        WechatFriendSaveBO wechatFriendSaveBO=new WechatFriendSaveBO();
        wechatFriendSaveBO.setCustomerWxId(oldFriendRelation.getCustomerWxId());
        wechatFriendSaveBO.setEmpWxId(oldFriendRelation.getEmpWxId());
        iUserPlatformAccountLogic.saveUserPlatformAccount(wechatFriendSaveBO);
        return true;
    }
}
