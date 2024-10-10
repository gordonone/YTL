package com.ytl.crm.service.ws.impl.friend;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytl.crm.domain.constant.Constants;
import com.ytl.crm.domain.entity.friend.WechatApplyQrcodeLogEntity;
import com.ytl.crm.domain.entity.friend.WechatFriendChangeEventEntity;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.friend.WechatFriendRelationStatusEnum;
import com.ytl.crm.domain.enums.friend.WsFriendChangeTypeEnum;
import com.ytl.crm.domain.req.work.ConvertExternalUserIdReq;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;
import com.ytl.crm.domain.resp.work.WorkWechatExternalUserIdResp;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsUserDetailResp;
import com.ytl.crm.service.ws.define.friend.IWechatAggregateService;
import com.ytl.crm.service.ws.define.friend.IWechatApplyQrcodeLogService;
import com.ytl.crm.service.ws.define.friend.IWechatFriendChangeEventService;
import com.ytl.crm.service.ws.define.friend.IWechatFriendRelationService;
import com.ytl.crm.utils.PreconditionsUtils;
import com.ytl.crm.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;


/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 9:28
 */
@Slf4j
@Component
public class WechatFriendLogicService {

    @Resource
    private IWechatFriendChangeEventService wechatFriendChangeEventService;

    @Resource
    private IWechatAggregateService wechatAggregateService;

    @Resource
    private IWechatApplyQrcodeLogService wechatApplyQrcodeLogService;

    @Resource
    private IWechatFriendRelationService wechatFriendRelationService;

    @Resource
    private WsConsumerHelper wsConsumerHelper;

    @Resource
    private WsConsumer wsConsumer;

    @Resource
    private WorkWechatConsumerHelper workWechatConsumerHelper;

    @Resource
    private WorkWeChatConsumer workWeChatConsumer;
    //
//    @Value("${delay.second.add.group:60}")
//    private Long sendAddGroupDelayTime;
//
    @Value("${workWeChatAgentId:1000038}")
    private Integer workWeChatAgentId;

    /**
     * 处理好友变更事件
     *
     * @param messageId 消息标识
     * @param content   事件内容
     */
    public void processFriendChangeEvent(String messageId, String content) {
        //判断消息是否已经处理
        if (messageHasProcessed(messageId)) {
            log.info("消息：{}已处理", messageId);
            return;
        }

        WsFriendChangeEvent changeEvent = JSONObject.parseObject(content, WsFriendChangeEvent.class);
        if (Objects.isNull(changeEvent) || changeEvent.isInvalidEvent()) {
            log.warn("好友变更事件解析失败，事件内容：{}", content);
            return;
        }

        //将此标识传递下去
        changeEvent.setMessageId(messageId);

        //获取类型
        WsFriendChangeTypeEnum changeTypeEnum = WsFriendChangeTypeEnum.parseByCode(changeEvent.getChangeType());
        switch (changeTypeEnum) {
            case ADD:
                processAddFriendEvent(changeEvent);
                break;
            case EDIT:
                processEditFriendEvent(changeEvent);
                break;
            case DEL_USER:
            case DEL_EMP:
                processDelFriendEvent(changeEvent);
                break;
            default:
                log.warn("好友变更事件类型解析失败，事件内容：{}", content);
        }
    }

    /**
     * 消息是否已经处理过
     *
     * @param messageId 消息标识
     * @return 是否处理过
     */
    private boolean messageHasProcessed(String messageId) {
        LambdaQueryWrapper<WechatFriendChangeEventEntity> queryWrapper = Wrappers.lambdaQuery(WechatFriendChangeEventEntity.class).select(WechatFriendChangeEventEntity::getId).eq(WechatFriendChangeEventEntity::getMessageId, messageId).eq(WechatFriendChangeEventEntity::getIsDelete, YesOrNoEnum.NO.getCode()).last("limit 1");

        WechatFriendChangeEventEntity changeEvent = wechatFriendChangeEventService.getOne(queryWrapper);
        return Objects.nonNull(changeEvent);
    }

    /**
     * 处理删除好友事件
     *
     * @param changeEvent 变更事件
     */
    private void processDelFriendEvent(WsFriendChangeEvent changeEvent) {
        //判断是否存在好友关系
        WechatFriendRelationEntity wechatFriendRelationEntity = queryFriendRelationByUserThirdId(changeEvent.getUserId(), changeEvent.getExternalUserId());
        if (Objects.isNull(wechatFriendRelationEntity)) {
            log.warn("删除好友事件消息体：{}中好友关系不存在", changeEvent);
            return;
        }

        wechatFriendRelationEntity.setStatus(WechatFriendRelationStatusEnum.DELETED.getCode());
        wechatAggregateService.updateWechatFriend(wechatFriendRelationEntity, changeEvent);
    }

    /**
     * 处理编辑好友事件
     *
     * @param changeEvent 变更事件
     */
    private void processEditFriendEvent(WsFriendChangeEvent changeEvent) {
        //判断是否存在好友关系
        WechatFriendRelationEntity wechatFriendRelationEntity = queryFriendRelationByUserThirdId(changeEvent.getUserId(), changeEvent.getExternalUserId());
        if (Objects.isNull(wechatFriendRelationEntity)) {
            log.warn("编辑好友事件消息体：{}中好友关系不存在", changeEvent);
            return;
        }

        //查询用户信息
        WsBaseResponse<WsUserDetailResp> useDetailResponse = wsConsumer.getUseDetail(wsConsumerHelper.acquireAccessToken(), changeEvent.getExternalUserId());
        WsUserDetailResp userDetailResp = ResponseUtils.parseWsBaseResponseWithData(useDetailResponse, "用户信息不存在");

        //获取指定关系的备注
        String remark = userDetailResp.getSpecialUserRemark(changeEvent.getUserId());
        if (StringUtils.isBlank(remark)) {
            log.info("消息体变更事件：{}，用户备注姓名为空，只保存事件", changeEvent);
            //保存事件
            wechatAggregateService.saveWsFriendChangeEvent(changeEvent);
            return;
        }

        //修改好友关系
        wechatFriendRelationEntity.setUserRemarkName(remark);
        wechatAggregateService.updateWechatFriend(wechatFriendRelationEntity, changeEvent);
    }

    /**
     * 处理添加好友事件
     *
     * @param changeEvent 添加好友事件
     */
    private void processAddFriendEvent(WsFriendChangeEvent changeEvent) {
        //根据state获取对应的申请记录
        WechatApplyQrcodeLogEntity applyQrcodeLogEntity = queryApplyLogByQrCodeId(changeEvent.getState());

        //判断是否已经是好友了
        if (Objects.nonNull(queryFriendRelationByUserThirdId(changeEvent.getUserId(), changeEvent.getExternalUserId()))) {
            log.info("员工：{}，客户：{}好友关系已添加", changeEvent.getUserId(), changeEvent.getExternalUserId());
            return;
        }

        //获取原始的微信标识
        String originalUserExternalId = convertUserExternal(changeEvent.getExternalUserId());

        //转换保存实体
        WechatFriendRelationEntity wechatFriendRelationEntity = buildWechatRelation(applyQrcodeLogEntity, changeEvent);

        //保存好友关系
        wechatFriendRelationEntity.setUserOriginalExternalId(originalUserExternalId);
        wechatAggregateService.saveWechatFriendRelation(wechatFriendRelationEntity, changeEvent);

//        //发送事件
//        AddFriendSuccessEvent event = AddFriendSuccessEvent.builder()
//                .contractCode(applyQrcodeLogEntity.getContractCode())
//                .contractType(applyQrcodeLogEntity.getContractType())
//                .resBlockId(applyQrcodeLogEntity.getResBlockId())
//                .virtualEmpId(applyQrcodeLogEntity.getVirtualEmpId())
//                .virtualEmpThirdId(applyQrcodeLogEntity.getVirtualEmpThirdId())
//                .uid(applyQrcodeLogEntity.getUid())
//                .userName(applyQrcodeLogEntity.getUserName())
//                .userExternalId(changeEvent.getExternalUserId())
//                .channelCode(applyQrcodeLogEntity.getChannelCode())
//                .build();
//        sender.send(JSONObject.toJSONString(event), sendAddGroupDelayTime, TimeUnit.SECONDS, FoxySceneKeyEnum.ADD_FRIEND_SUCCESS.getKey());
    }
//

    /**
     * 构建实体
     *
     * @param applyQrcodeLogEntity 申请活码实体
     * @param changeEvent          变更事件
     * @return 实体
     */
    private WechatFriendRelationEntity buildWechatRelation(WechatApplyQrcodeLogEntity applyQrcodeLogEntity, WsFriendChangeEvent changeEvent) {
        LambdaQueryWrapper<WechatFriendRelationEntity> queryWrapper = Wrappers.lambdaQuery(WechatFriendRelationEntity.class).select(WechatFriendRelationEntity::getId).eq(WechatFriendRelationEntity::getVirtualEmpThirdId, applyQrcodeLogEntity.getVirtualEmpThirdId()).eq(WechatFriendRelationEntity::getUserExternalId, changeEvent.getExternalUserId()).eq(WechatFriendRelationEntity::getStatus, WechatFriendRelationStatusEnum.DELETED.getCode()).last("limit 1");

        WechatFriendRelationEntity friendRelation = wechatFriendRelationService.getOne(queryWrapper);
        if (Objects.nonNull(friendRelation)) {
            friendRelation.setStatus(WechatFriendRelationStatusEnum.ADDED.getCode());
            friendRelation.setChannelCode(applyQrcodeLogEntity.getChannelCode());
            return friendRelation;
        } else {
            WechatFriendRelationEntity wechatFriendRelationEntity = new WechatFriendRelationEntity();
            wechatFriendRelationEntity.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
            wechatFriendRelationEntity.setChannelCode(applyQrcodeLogEntity.getChannelCode());
            wechatFriendRelationEntity.setUid(applyQrcodeLogEntity.getUid());
            wechatFriendRelationEntity.setVirtualEmpId(applyQrcodeLogEntity.getVirtualEmpId());
            wechatFriendRelationEntity.setVirtualEmpThirdId(changeEvent.getUserId());
            wechatFriendRelationEntity.setUserExternalId(changeEvent.getExternalUserId());
            wechatFriendRelationEntity.setStatus(WechatFriendRelationStatusEnum.ADDED.getCode());
            wechatFriendRelationEntity.setCreateUserCode(Constants.SYSTEM_CODE);
            wechatFriendRelationEntity.setCreateUserName(Constants.SYSTEM_NAME);
            wechatFriendRelationEntity.setModifyUserCode(Constants.SYSTEM_CODE);
            wechatFriendRelationEntity.setModifyUserName(Constants.SYSTEM_NAME);
            return wechatFriendRelationEntity;
        }
    }
//

    /**
     * 转换用户微信标识
     *
     * @param externalUserId 微信标识
     * @return 用户原始微信标识
     */
    private String convertUserExternal(String externalUserId) {
        ConvertExternalUserIdReq req = new ConvertExternalUserIdReq();
        req.setExternalUserid(externalUserId);
        req.setSourceAgentId(workWeChatAgentId);
        WorkWechatExternalUserIdResp userIdResp = workWeChatConsumer.serviceExternalUserIdToExternalUserId(workWechatConsumerHelper.acquireAccessToken(), req);
        PreconditionsUtils.checkBusiness(Objects.nonNull(userIdResp) && userIdResp.isOk(), "用户微信标识转换异常");
        PreconditionsUtils.checkBusiness(StringUtils.isNotBlank(userIdResp.getExternalUserId()), "用户微信标识转换异常");
        return userIdResp.getExternalUserId();
    }
//

    /**
     * 是否已经添加好友
     *
     * @param empThirdId     三方标识
     * @param userExternalId 用户外部联系人标识
     * @return 好友记录
     */
    private WechatFriendRelationEntity queryFriendRelationByUserThirdId(String empThirdId, String userExternalId) {
        LambdaQueryWrapper<WechatFriendRelationEntity> queryWrapper = Wrappers.lambdaQuery(WechatFriendRelationEntity.class).select(WechatFriendRelationEntity::getId).eq(WechatFriendRelationEntity::getVirtualEmpThirdId, empThirdId).eq(WechatFriendRelationEntity::getUserExternalId, userExternalId).eq(WechatFriendRelationEntity::getStatus, WechatFriendRelationStatusEnum.ADDED.getCode()).last("limit 1");

        return wechatFriendRelationService.getOne(queryWrapper);
    }

    /**
     * 查询申请记录
     *
     * @param state 活码标识
     * @return 活码申请记录
     */
    private WechatApplyQrcodeLogEntity queryApplyLogByQrCodeId(String state) {
        PreconditionsUtils.checkBusiness(StringUtils.isNotBlank(state), "活码标识为空");
        LambdaQueryWrapper<WechatApplyQrcodeLogEntity> queryWrapper = Wrappers.lambdaQuery(WechatApplyQrcodeLogEntity.class).select(WechatApplyQrcodeLogEntity::getVirtualEmpId, WechatApplyQrcodeLogEntity::getVirtualEmpThirdId, WechatApplyQrcodeLogEntity::getUid, WechatApplyQrcodeLogEntity::getUserName, WechatApplyQrcodeLogEntity::getId, WechatApplyQrcodeLogEntity::getChannelCode).eq(WechatApplyQrcodeLogEntity::getState, state).eq(WechatApplyQrcodeLogEntity::getIsDelete, YesOrNoEnum.NO.getCode()).last("limit 1");
        WechatApplyQrcodeLogEntity one = wechatApplyQrcodeLogService.getOne(queryWrapper);
        PreconditionsUtils.checkBusiness(Objects.nonNull(one), "未能识别的的标识：%s", state);
        return one;
    }
}
