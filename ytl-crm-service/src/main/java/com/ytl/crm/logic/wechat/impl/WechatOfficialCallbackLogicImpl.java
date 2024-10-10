package com.ytl.crm.logic.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.ytl.crm.domain.entity.wechat.WechatFriendChangeEventEntity;
import com.ytl.crm.domain.enums.wechat.WechatEventStatusEnum;
import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import com.ytl.crm.domain.enums.wechat.official.WeChatOfficialChangeTypeEnum;
import com.ytl.crm.domain.enums.wechat.official.WeChatOfficialEventEnum;
import com.ytl.crm.event.wechat.model.WeChatEvent;
import com.ytl.crm.event.wechat.model.customer.FriendEvent;
import com.ytl.crm.event.wechat.pulisher.WeChatEventPublisher;
import com.ytl.crm.help.wechat.WxOfficialCallBackHelper;
import com.ytl.crm.mq.model.wechat.official.FriendEventMsgData;
import com.ytl.crm.mq.model.wechat.official.WxOfficialCallbackMsgDTO;
import com.ytl.crm.service.ws.define.wechat.IWechatEmpLogic;
import com.ytl.crm.service.ws.define.wechat.IWechatOfficialCallbackLogic;
import com.ytl.crm.service.ws.define.wechat.official.IWechatFriendChangeEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatOfficialCallbackLogicImpl implements IWechatOfficialCallbackLogic {

    private final WxOfficialCallBackHelper wxOfficialCallBackHelper;
    private final WeChatCallBackProducer weChatCallBackProducer;

    private final WeChatCallbackConfig weChatCallbackConfig;
    private final WeChatEventPublisher weChatEventPublisher;
    private final IWechatEmpLogic weChatEmpLogic;
    private final IWechatFriendChangeEventService iWechatFriendChangeEventService;

    @Override
    public void handleCallBack(String timestamp, String none, String msgData) {
        //解密后消息体xml转json
        JSONObject data = wxOfficialCallBackHelper.transMsgToJson(msgData);

        //简单校验，避免不必要的逻辑处理，也可以把员工校验加上
        final String event = data.getString(WxOfficialCallbackMsgDTO.FILED_EVENT);
        final String changeType = data.getString(WxOfficialCallbackMsgDTO.FILED_CHANGE_TYPE);
        boolean isConfigChange = isConfigChange(event, changeType);
        if (!isConfigChange) {
            log.info("当前事件类型未配置，无需处理，event={}，changeType={}", event, changeType);
            return;
        }

//        //构建 & 发送MQ
//        String traceId = CodeGeneratorUtils.nextUUIdWithDate("OF_E");
//        WxOfficialCallbackMsgDTO msgDTO = new WxOfficialCallbackMsgDTO();
//        msgDTO.setTraceId(traceId);
//        msgDTO.setData(data);
//        msgDTO.setTimestamp(timestamp);
//        msgDTO.setNonce(none);
//        weChatCallBackProducer.sendOfficialCallbackMsg(msgDTO);
    }

    @Override
    public void handleCallbackMsg(WxOfficialCallbackMsgDTO msgDTO) {
        //判断是否需要处理
        boolean needHandle = checkNeedHandle(msgDTO);
        if (!needHandle) {
            return;
        }

        //代码层面是否实现对应的事件处理
        JSONObject callBackData = msgDTO.getData();
        final String event = callBackData.getString(WxOfficialCallbackMsgDTO.FILED_EVENT);
        final String changeType = callBackData.getString(WxOfficialCallbackMsgDTO.FILED_CHANGE_TYPE);
        WeChatOfficialChangeTypeEnum changeTypeEnum = confirmChangeType(event, changeType);
        if (changeTypeEnum == null) {
            log.info("不支持的回调事件变更类型，event={}，changeType={}", event, changeType);
            return;
        }

        //构建event + 持久化 + 发布事件
        WeChatEvent weChatEvent = buildWechatEvent(changeTypeEnum, msgDTO);
        if (weChatEvent != null) {
            weChatEventPublisher.postWeChatEvent(weChatEvent);
        }
    }

    private boolean checkNeedHandle(WxOfficialCallbackMsgDTO msgDTO) {
        JSONObject callBackData = msgDTO.getData();
        if (callBackData == null) {
            log.warn("消息体data部分为空");
            return false;
        }
        //apollo配置关心的事件类型
        final String event = callBackData.getString(WxOfficialCallbackMsgDTO.FILED_EVENT);
        final String changeType = callBackData.getString(WxOfficialCallbackMsgDTO.FILED_CHANGE_TYPE);
        boolean isConfigChange = isConfigChange(event, changeType);
        if (!isConfigChange) {
            log.info("当前事件类型未配置，无需处理，event={}，changeType={}", event, changeType);
        }
        return isConfigChange;
    }

    private boolean isConfigChange(String event, String changeType) {
        if (StringUtils.isAnyBlank(event, changeType)) {
            return false;
        }
        //基于事件类型进行分发，如果是不关注的事件类型，无需处理
        Map<String, List<String>> officialSupportEventMap = weChatCallbackConfig.getOfficialSupportEventMap();
        if (Check.isNullOrEmpty(officialSupportEventMap)) {
            return false;
        }
        List<String> changeTypeList = officialSupportEventMap.getOrDefault(event, Collections.emptyList());
        if (Check.isNullOrEmpty(changeTypeList)) {
            return false;
        }
        return changeTypeList.contains(changeType);
    }

    private WeChatOfficialChangeTypeEnum confirmChangeType(String event, String changeType) {
        //外部联系人变更事件
        WeChatOfficialEventEnum eventEnum = WeChatOfficialEventEnum.queryByCode(event);
        if (eventEnum == null) {
            return null;
        }
        return WeChatOfficialChangeTypeEnum.queryChangeType(eventEnum, changeType);
    }

    private WeChatEvent buildWechatEvent(WeChatOfficialChangeTypeEnum changeTypeEnum, WxOfficialCallbackMsgDTO msgDTO) {
        WeChatOfficialEventEnum eventType = changeTypeEnum.getEventType();
        //目前只处理外部联系人事件
        if (WeChatOfficialEventEnum.CHANGE_EXTERNAL_CONTACT.equals(eventType)) {
            return buildFriendEvent(changeTypeEnum, msgDTO);
        }
        return null;
    }


    private FriendEvent buildFriendEvent(WeChatOfficialChangeTypeEnum changeTypeEnum, WxOfficialCallbackMsgDTO msgDTO) {
        FriendEventMsgData msgData = (FriendEventMsgData) msgDTO.getData().toJavaObject(changeTypeEnum.getDataClazz());
        String empWxId = msgData.empWxId();
        boolean isCareAboutEmp = weChatEmpLogic.isCareAboutEmpWxId(empWxId);
        if (!isCareAboutEmp) {
            log.info("不是系统配置的微信id，无需处理其外部联系人事件，empWxId={}", empWxId);
            return null;
        }
        WechatSourceEnum sourceEnum = WechatSourceEnum.OFFICIAL;
        FriendEvent event = msgData.toEvent();
        //事件时间
        if (StringUtils.isNumeric(msgDTO.getTimestamp())) {
            long timestamp = Long.parseLong(msgDTO.getTimestamp());
            event.setCreateTime(new Date(timestamp * 1000L));
        } else {
            event.setCreateTime(new Date());
        }
        event.setTraceId(msgDTO.getTraceId());
        event.setSource(sourceEnum);
        event.setOriginMsg(JSON.toJSONString(msgDTO));

        //持久化事件，这里是否需要去重
        WechatFriendChangeEventEntity eventEntity = buildFriendEventEntity(event);
        //保存事件
        boolean saveRet = iWechatFriendChangeEventService.save(eventEntity);
        log.info("保存事件记录，saveRet={}", saveRet);
        if (!saveRet) {
            //todo这里如果保存失败，怎么办？
            log.info("保存事件记录异常，traceId={}", msgDTO.getTraceId());
            return null;
        }
        event.setFriendEventLogicCode(eventEntity.getLogicCode());
        return event;
    }

    private WechatFriendChangeEventEntity buildFriendEventEntity(FriendEvent event) {
        WechatFriendChangeEventEntity changeEntity = new WechatFriendChangeEventEntity();
        changeEntity.setTraceId(event.getTraceId());
        changeEntity.setEventType(event.getEventType().getCode());
        changeEntity.setEventStatus(WechatEventStatusEnum.INIT.getCode());
        changeEntity.setEmpWxId(event.getEmpWxId());
        changeEntity.setCustomerWxId(event.getCustomerWxId());
        changeEntity.setOriginMsg(event.getOriginMsg());
        changeEntity.setEventSource(event.getSource().getCode());
        return changeEntity;
    }

}
