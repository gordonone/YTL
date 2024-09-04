package com.ytl.crm.service.ws.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.domain.constant.Constants;
import com.ytl.crm.domain.entity.friend.WechatApplyQrcodeLogEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.req.friend.WechatQrCodeApplyReq;
import com.ytl.crm.domain.req.ws.WsApplyQrCodeReq;
import com.ytl.crm.domain.req.ws.WsQrCodeDetailReq;
import com.ytl.crm.domain.resp.friend.WechatQrCodeApplyResp;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsQrCodeDetailResp;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.service.ws.define.friend.IWechatApplyQrcodeLogService;
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

    @Value("${defaultCreateUserId:woDkH9EAAAdZ-reBenrwqnQvST0OxVmw}")
    private String defaultCreateUserId;


    /**
     * 查询二维码信息
     *
     * @param req 查询入参
     * @return 二维码信息
     */
    public WechatQrCodeApplyResp queryQrCode(WechatQrCodeApplyReq req) {
        LambdaQueryWrapper<WechatApplyQrcodeLogEntity> queryWrapper = Wrappers.lambdaQuery(WechatApplyQrcodeLogEntity.class).select(WechatApplyQrcodeLogEntity::getQrcodeUrl, WechatApplyQrcodeLogEntity::getVirtualEmpName, WechatApplyQrcodeLogEntity::getVirtualEmpAvatar, WechatApplyQrcodeLogEntity::getUserName, WechatApplyQrcodeLogEntity::getQrcodeId, WechatApplyQrcodeLogEntity::getId, WechatApplyQrcodeLogEntity::getUid, WechatApplyQrcodeLogEntity::getContractCode, WechatApplyQrcodeLogEntity::getVirtualEmpThirdId, WechatApplyQrcodeLogEntity::getUserRemarkName).eq(WechatApplyQrcodeLogEntity::getLogicCode, req.getApplyCode()).eq(WechatApplyQrcodeLogEntity::getIsDelete, YesOrNoEnum.NO.getCode()).orderByDesc(WechatApplyQrcodeLogEntity::getCreateTime).last("limit 1");

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


}
