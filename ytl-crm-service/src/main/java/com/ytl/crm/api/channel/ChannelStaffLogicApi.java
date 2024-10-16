package com.ytl.crm.api.channel;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ytl.crm.domain.bo.channel.*;
import com.ytl.crm.domain.bo.wechat.ChannelQrCodeApplyBO;
import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.StaffChannelCodeEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.wechat.CustomerWeChatQrCodeDTO;
import com.ytl.crm.logic.channel.interfaces.IChannelStaffLogic;
import com.ytl.crm.logic.wechat.interfaces.IWechatQrCodeLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Api(value = "员工渠道信息", tags = "员工渠道信息")
@RestController
@RequestMapping("/channel/staff")
@RequiredArgsConstructor
public class ChannelStaffLogicApi {

    private final IChannelStaffLogic iChannelStaffLogic;
    private final IWechatQrCodeLogic iWechatQrCodeLogic;

    @PostMapping("/page")
    @ApiOperation(value = "获取渠道员工-分页列表")
    public BaseResponse<PageResp<StaffBaseBo>> getChannelStaff(@RequestBody @Valid StaffAccountPageBo staffAccountPageBo) {
        return BaseResponse.responseOk(iChannelStaffLogic.getChannelStaff(staffAccountPageBo));
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取渠道员工-搜索列表")
    public BaseResponse<List<StaffBaseBo>> getChannelStaffList(@RequestBody @Valid StaffAccountSearchBo staffAccountSearchBo) {
        log.info("获取渠道员工-搜索列表:{}", JSON.toJSONString(staffAccountSearchBo));
        return BaseResponse.responseOk(iChannelStaffLogic.getChannelStaffList(staffAccountSearchBo));
    }


    @PostMapping("/detail")
    @ApiOperation(value = "获取渠道员工-详情")
    public BaseResponse<StaffAccountBo> getStaffAccountBo(@RequestBody @Valid StaffAccountDetailBo staffAccountDetailBo) {
        return BaseResponse.responseOk(iChannelStaffLogic.getStaffAccountBo(staffAccountDetailBo));
    }


    @PostMapping("/add")
    @ApiOperation(value = "添加修改渠道员工")
    public BaseResponse<Boolean> saveChannelStaff(@RequestBody @Valid StaffBaseBo staffBaseBo) {
        return BaseResponse.responseOk(iChannelStaffLogic.saveChannelStaff(staffBaseBo));
    }


    @PostMapping("/add/live/code")
    @ApiOperation(value = "添加员工活码")
    public BaseResponse<Boolean> saveStaffChannelCode(@RequestBody @Valid StaffPlatformChannelSaveBo staffPlatformChannelSaveBo) {
        iChannelStaffLogic.saveStaffChannelCode(staffPlatformChannelSaveBo);
        return BaseResponse.responseOk();
    }

    @PostMapping("/get/live/code")
    @ApiOperation(value = "获取加好友二维码")
    public BaseResponse<String> getStaffChannelCode(@RequestBody @Valid StaffChannelLiveBo staffChannelLiveBo) {
        StaffChannelCodeEntity staffChannelCodeEntity = iChannelStaffLogic.getLiveCode(staffChannelLiveBo);
        if (StringUtils.isBlank(staffChannelCodeEntity.getApplyQrCode())) {
            ChannelQrCodeApplyBO channelQrCodeApplyBO = new ChannelQrCodeApplyBO();
            channelQrCodeApplyBO.setChannelCode(staffChannelCodeEntity.getChannelCode() + "");
            channelQrCodeApplyBO.setUniqueKey(staffChannelCodeEntity.getLogicCode());
            channelQrCodeApplyBO.setEmpWxId(staffChannelCodeEntity.getExternalId());
            WechatQrcodeApplyLogEntity applyLog = iWechatQrCodeLogic.applyChannelQrCode(channelQrCodeApplyBO);
            //修改申请二维码主键
            staffChannelCodeEntity.setApplyQrCode(applyLog.getLogicCode());
            iChannelStaffLogic.saveStaffApplyChannelCode(staffChannelCodeEntity);
        }

        CustomerWeChatQrCodeDTO qrCodeDTO = iWechatQrCodeLogic.queryQrCodeByApplyCode(staffChannelCodeEntity.getApplyQrCode(), WechatSourceEnum.OFFICIAL.getCode());
        return BaseResponse.responseOk(qrCodeDTO.getQrCodeUrl());
    }


}
