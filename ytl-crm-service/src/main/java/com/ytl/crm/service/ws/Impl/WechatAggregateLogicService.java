package com.ytl.crm.service.ws.Impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.domain.constant.Constants;
import com.ytl.crm.domain.entity.friend.WechatApplyQrcodeLogEntity;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.friend.WechatFriendRelationStatusEnum;
import com.ytl.crm.domain.req.friend.WechatFriendRelationReq;
import com.ytl.crm.domain.req.friend.WechatQrCodeApplyReq;
import com.ytl.crm.domain.req.ws.WsApplyQrCodeReq;
import com.ytl.crm.domain.req.ws.WsQrCodeDetailReq;
import com.ytl.crm.domain.resp.friend.WechatFriendRelationResp;
import com.ytl.crm.domain.resp.friend.WechatQrCodeApplyResp;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsQrCodeDetailResp;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.service.ws.define.friend.IWechatApplyQrcodeLogService;
import com.ytl.crm.service.ws.define.friend.IWechatEmpMappingService;
import com.ytl.crm.service.ws.define.friend.IWechatFriendRelationService;
import com.ytl.crm.utils.PreconditionsUtils;
import com.ytl.crm.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 9:37
 * 企微聚合服务
 */
@Slf4j
@Component
public class WechatAggregateLogicService {


    @Resource
    private WsConsumer wsConsumer;

    @Resource
    private WsConsumerHelper wsConsumerHelper;

    @Resource
    private IWechatApplyQrcodeLogService wechatApplyQrcodeLogService;

    @Resource
    private IWechatEmpMappingService wechatEmpMappingService;

    @Resource
    private IWechatFriendRelationService wechatFriendRelationService;

    @Value("${defaultCreateUserId:woDkH9EAAAdZ-reBenrwqnQvST0OxVmw}")
    private String defaultCreateUserId;


    /**
     * 查询二维码信息
     *
     * @param req 查询入参
     * @return 二维码信息
     */
    public WechatQrCodeApplyResp queryQrCode(WechatQrCodeApplyReq req) {
        LambdaQueryWrapper<WechatApplyQrcodeLogEntity> queryWrapper = Wrappers.lambdaQuery(WechatApplyQrcodeLogEntity.class)
                .select(WechatApplyQrcodeLogEntity::getQrcodeUrl, WechatApplyQrcodeLogEntity::getVirtualEmpName, WechatApplyQrcodeLogEntity::getVirtualEmpAvatar, WechatApplyQrcodeLogEntity::getUserName, WechatApplyQrcodeLogEntity::getQrcodeId, WechatApplyQrcodeLogEntity::getId, WechatApplyQrcodeLogEntity::getUid, WechatApplyQrcodeLogEntity::getVirtualEmpThirdId, WechatApplyQrcodeLogEntity::getUserRemarkName)
                .eq(WechatApplyQrcodeLogEntity::getLogicCode, req.getApplyCode()).eq(WechatApplyQrcodeLogEntity::getIsDelete, YesOrNoEnum.NO.getCode())
                .orderByDesc(WechatApplyQrcodeLogEntity::getCreateTime).last("limit 1");

        WechatApplyQrcodeLogEntity applyQrcodeLog = wechatApplyQrcodeLogService.getOne(queryWrapper);
        PreconditionsUtils.checkBusiness(Objects.nonNull(applyQrcodeLog), "非法的活码标识");

        if (StringUtils.isBlank(applyQrcodeLog.getQrcodeUrl())) {
            //申请活码
            String qrCodeId = applyQrCodeIdFromRemote(applyQrcodeLog);

            //获取活码详情
            WsQrCodeDetailResp wsQrCodeDetailResp = queryQrCodeFromRemote(qrCodeId);
            WechatApplyQrcodeLogEntity updateEntity = new WechatApplyQrcodeLogEntity();
            updateEntity.setId(applyQrcodeLog.getId());
            updateEntity.setQrcodeId(qrCodeId);
            updateEntity.setQrcodeUrl(wsQrCodeDetailResp.getQrCodeImgUrl());
            updateEntity.setState(wsQrCodeDetailResp.getState());
            boolean b = wechatApplyQrcodeLogService.updateById(updateEntity);
            log.info("更新活码url结果：{}", b);
            applyQrcodeLog.setQrcodeUrl(wsQrCodeDetailResp.getQrCodeImgUrl());
        }

        return WechatQrCodeApplyResp.buildByApplyQrCodeLog(applyQrcodeLog);
    }

    /**
     * 申请活码
     *
     * @param applyQrcodeLog 申请活码记录
     * @return 活码标识
     */
    private String applyQrCodeIdFromRemote(WechatApplyQrcodeLogEntity applyQrcodeLog) {
        //用户
        WsApplyQrCodeReq.User user = new WsApplyQrCodeReq.User();
        user.setUserId(applyQrcodeLog.getVirtualEmpThirdId());

        //TODO 欢迎语
        WsApplyQrCodeReq.Greeting greeting = new WsApplyQrCodeReq.Greeting();
        greeting.setGreeting("好友欢迎语！！");

        //构建入参
        WsApplyQrCodeReq wsApplyQrCodeReq = WsApplyQrCodeReq.builder().userId(defaultCreateUserId).withoutConfirm(Constants.YES_I).type(Constants.YES_I).logoImg(applyQrcodeLog.getVirtualEmpAvatar()).qrcodeName(applyQrcodeLog.getVirtualEmpName()).customRemark(applyQrcodeLog.getUserRemarkName()).greetingList(Lists.newArrayList(greeting)).userList(Lists.newArrayList(user)).staffPromotionFlag(Constants.NO_I).build();

        log.info("申请活码入参：{}", wsApplyQrCodeReq);
        WsBaseResponse<String> applyResult = wsConsumer.applyQrCode(wsConsumerHelper.acquireAccessToken(), wsApplyQrCodeReq);
        log.info("申请活码出参：{}", applyResult);
        String qrCodeId = ResponseUtils.parseWsBaseResponseWithData(applyResult, "申请活码失败");
        PreconditionsUtils.checkBusiness(StringUtils.isNotBlank(qrCodeId), "申请活码失败");
        return qrCodeId;
    }


    /**
     * 从三方查询活码
     *
     * @param qrCodeId 活码标识
     * @return 活码url
     */
    public WsQrCodeDetailResp queryQrCodeFromRemote(String qrCodeId) {
        WsQrCodeDetailReq req = new WsQrCodeDetailReq();
        req.setId(qrCodeId);
        log.info("获取活码详情入参：{}", req);
        WsBaseResponse<WsQrCodeDetailResp> wsBaseResponse = wsConsumer.queryQrCodeDetail(wsConsumerHelper.acquireAccessToken(), req);
        log.info("获取活码详情出参：{}", wsBaseResponse);
        return ResponseUtils.parseWsBaseResponseWithData(wsBaseResponse, "加载二维码异常，请稍后重试");
    }


    /**
     * 查询好友关系
     *
     * @param req 查询入参
     * @return 好友关系
     */
    public WechatFriendRelationResp queryFriendRelation(WechatFriendRelationReq req) {

        //保存申请记录
        String qrCodeId = saveApplyQrCode(req);

        //查询小乐管家是否与用户是好友
        WechatFriendRelationResp wechatFriendRelationResp = ensureVirtualAndUserAddFriend(req);
        if (Objects.nonNull(wechatFriendRelationResp)) {
            wechatFriendRelationResp.setApplyCode(qrCodeId);
            return wechatFriendRelationResp;
        }
        return WechatFriendRelationResp.buildHasAddFriendResp(req.getEmpUid(), qrCodeId);
    }

    /**
     * 判断虚拟管家和用户是否添加好友
     *
     * @return 是否添加好友
     */
    private WechatFriendRelationResp ensureVirtualAndUserAddFriend(WechatFriendRelationReq req) {
        LambdaQueryWrapper<WechatFriendRelationEntity> queryWrapper = Wrappers.lambdaQuery(WechatFriendRelationEntity.class).select(WechatFriendRelationEntity::getId).eq(WechatFriendRelationEntity::getVirtualEmpId, req.getEmpUid()).eq(WechatFriendRelationEntity::getUid, req.getFriendUid()).eq(WechatFriendRelationEntity::getStatus, WechatFriendRelationStatusEnum.ADDED.getCode()).last("limit 1");

        WechatFriendRelationEntity friendRelation = wechatFriendRelationService.getOne(queryWrapper);
        if (Objects.nonNull(friendRelation)) {
            return WechatFriendRelationResp.buildHasAddFriendResp(friendRelation.getVirtualEmpId());
        }

        return null;
    }

    /**
     * 申请活码
     *
     * @return 活码标识
     */
    private String saveApplyQrCode(WechatFriendRelationReq req) {
        //获取映射的企微标识
        // String empThirdId = wechatEmpMappingService.getEmpThirdIdByEmpId(req.getEmpUid());

        //empThirdId ws唯一标识
        //微盛客户编码
        //req.getFriendUid()
        //保存入库
        WechatApplyQrcodeLogEntity applyQrcodeLogEntity = WechatApplyQrcodeLogEntity.builder()
                .logicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()))
                .channelCode("XHR")
                .uid(req.getFriendUid())
                .userName("XHRXHRXHR")
                .userRemarkName("备注备注")
                .virtualEmpId(req.getEmpUid())
                .virtualEmpThirdId(req.getEmpUid())
                .virtualEmpName(req.getEmpUid())
                //  .virtualEmpAvatar(relation.getVirtualAvatar())
                .createUserCode(Constants.SYSTEM_CODE)
                .createUserName(Constants.SYSTEM_NAME)
                .modifyUserCode(Constants.SYSTEM_CODE)
                .modifyUserName(Constants.SYSTEM_NAME).build();

        boolean save = wechatApplyQrcodeLogService.save(applyQrcodeLogEntity);
        log.info("申请记录保存结果：{}", save);
        return applyQrcodeLogEntity.getLogicCode();
    }


}
