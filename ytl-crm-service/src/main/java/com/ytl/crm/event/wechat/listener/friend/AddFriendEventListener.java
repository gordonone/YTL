package com.ytl.crm.event.wechat.listener.friend;

import com.google.common.eventbus.Subscribe;
import com.ytl.crm.config.wechat.WeChatCallbackConfig;
import com.ytl.crm.consumer.resp.wechat.ExternalContactQueryResp;
import com.ytl.crm.consumer.wechat.WxOfficialConsumerHelper;
import com.ytl.crm.domain.bo.wechat.WechatFriendSaveBO;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeEntity;
import com.ytl.crm.event.wechat.model.customer.AddFriendEvent;
import com.ytl.crm.event.wechat.model.customer.FriendEventContext;
import com.ytl.crm.logic.customer.interfaces.IUserPlatformAccountLogic;
import com.ytl.crm.logic.wechat.interfaces.IWechatFriendLogic;
import com.ytl.crm.service.interfaces.wechat.official.IWechatFriendRelationService;
import com.ytl.crm.service.interfaces.wechat.official.IWechatQrcodeApplyLogService;
import com.ytl.crm.service.interfaces.wechat.official.IWechatQrcodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddFriendEventListener extends AbstractWeChatFriendListener<AddFriendEvent> {

    private final IWechatQrcodeService iWechatQrcodeService;
    private final IUserPlatformAccountLogic iUserPlatformAccountLogic;
    private final IWechatQrcodeApplyLogService iWechatQrcodeApplyLogService;
    private final IWechatFriendLogic iWechatFriendLogic;
    private final IWechatFriendRelationService iWechatFriendRelationService;
    private final WeChatCallbackConfig weChatCallbackConfig;
    private final WxOfficialConsumerHelper wxOfficialConsumerHelper;


    @Subscribe
    @Override
    public void listen(AddFriendEvent event) {
        super.handleEvent(event);
    }

    @Override
    public FriendEventContext<AddFriendEvent> buildContext(AddFriendEvent event) {
        FriendEventContext<AddFriendEvent> context = FriendEventContext.of(event);
        String state = event.getState();
        if (StringUtils.isBlank(event.getState())) {
            return context;
        }
        //判断state和对应的
        String wechatSource = event.getSource().getCode();
        WechatQrcodeEntity qrCodeEntity = iWechatQrcodeService.queryByStateCode(state, wechatSource);
        if (qrCodeEntity != null) {
            String applyCode = qrCodeEntity.getApplyCode();
            WechatQrcodeApplyLogEntity applyLog = iWechatQrcodeApplyLogService.queryByLogicCode(applyCode);
            context.setApplyLogEntity(applyLog);
            context.setQrcodeEntity(qrCodeEntity);
        }

        //历史好友关系
        String customerWxId = event.getCustomerWxId();
        String empWxId = event.getEmpWxId();
        WechatFriendRelationEntity oldRelation = iWechatFriendRelationService.queryByCustomerAndEmpId(customerWxId, empWxId);
        context.setOldFriendRelation(oldRelation);
        return context;
    }


    @Override
    public boolean checkNeedHandle(FriendEventContext<AddFriendEvent> context) {
        AddFriendEvent event = context.getEvent();
        //判断state和被添加人是否在系统中
        String state = event.getState();
        if (StringUtils.isBlank(state)) {
            log.info("state为空，无需处理");
            return false;
        }

        WechatQrcodeEntity qrCodeEntity = context.getQrcodeEntity();
        if (qrCodeEntity == null) {
            log.info("未知的state，无需处理，state={}", state);
            return false;
        }
        WechatQrcodeApplyLogEntity applyLog = context.getApplyLogEntity();
        if (applyLog == null) {
            log.info("未找到对应的applyLog，无需处理");
            return false;
        }

        //判断下面的逻辑
        String eventEmpWxId = event.getEmpWxId();
        String customerWxId = event.getCustomerWxId();
        if (StringUtils.isAnyBlank(eventEmpWxId, customerWxId)) {
            log.info("客户wxId或者员工wxId为空，无需处理");
            return false;
        }

        WechatFriendRelationEntity oldRelation = context.getOldFriendRelation();
        if (oldRelation != null) {
            log.info("好友关系已存在，无需处理");
            return false;
        }

        String applyLogEmpWxId = applyLog.getEmpWxId();
        //检验是否是同一个企微官方id，避免state和别的地方重复的问题
        return StringUtils.isNotBlank(eventEmpWxId) && StringUtils.equalsIgnoreCase(eventEmpWxId, applyLogEmpWxId);
    }

    @Override
    public boolean doHandle(FriendEventContext<AddFriendEvent> context) {
        //发送欢迎语 - 附属操作失败不处理
        WechatQrcodeApplyLogEntity applyLogEntity = context.getApplyLogEntity();
        AddFriendEvent event = context.getEvent();
        ExternalContactQueryResp contactDetail = wxOfficialConsumerHelper.queryContactDetail(event.getCustomerWxId());
        // todo 发送欢迎语
        //  sendWelcomeMsg(event, applyLogEntity, contactDetail);


        WechatFriendSaveBO saveBO = new WechatFriendSaveBO();
        saveBO.setApplyCode(applyLogEntity.getLogicCode());
        saveBO.setCustomerWxId(event.getCustomerWxId());
        saveBO.setEmpWxId(event.getEmpWxId());
        saveBO.setAddTime(event.getCreateTime());
        saveBO.setContactDetail(contactDetail);
        boolean isOk = iWechatFriendLogic.saveFriend(saveBO);
        saveAccount(saveBO);
        return isOk;
    }

//    private void sendWelcomeMsg(AddFriendEvent event,
//                                WechatQrcodeApplyLogEntity applyLogEntity,
//                                ExternalContactQueryResp contactDetail) {
//        try {
//            //20秒有效
//            String welcomeCode = event.getWelcomeCode();
//            String channelCode = applyLogEntity.getChannelCode();
//            if (StringUtils.isAnyBlank(welcomeCode, channelCode)) {
//                return;
//            }
//            WeChatWelcomeMsgReq msgReq = weChatCallbackConfig.acquireWelcomeMsg(channelCode);
//            if (msgReq == null) {
//                return;
//            }
//
//            ExternalUserContactDTO externalContact = contactDetail.getExternalContact();
//            fillCustomerName(msgReq, externalContact);
//            msgReq.setWelcomeCode(welcomeCode);
//            wxOfficialConsumerHelper.sendAddFriendWelcomeMsg(msgReq);
//        } catch (Exception e) {
//            log.error("发送欢迎语异常", e);
//        }
//
//    }

//    private void fillCustomerName(WeChatWelcomeMsgReq msgReq, ExternalUserContactDTO externalContact) {
//        String nickName = externalContact != null ? externalContact.getName() : "自如客";
//        MsgText msgText = msgReq.getText();
//        if (msgText != null) {
//            String content = msgText.getContent();
//            msgText.setContent(content.replace("#客户昵称#", nickName));
//        }
//    }

    private void saveAccount(WechatFriendSaveBO saveBO) {
        try {
            iUserPlatformAccountLogic.saveUserPlatformAccount(saveBO);
        } catch (Exception e) {
            log.error("保存平台客户账号异常", e);
        }
    }


}
