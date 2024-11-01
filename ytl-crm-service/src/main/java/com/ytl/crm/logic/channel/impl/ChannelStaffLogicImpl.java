package com.ytl.crm.logic.channel.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.ytl.crm.common.exception.UgcCrmServiceException;
import com.ytl.crm.config.channel.ChannelQrCodeApolloConfig;
import com.ytl.crm.domain.bo.channel.*;
import com.ytl.crm.domain.bo.wechat.ChannelQrCodeApplyBO;
import com.ytl.crm.domain.entity.channel.ChannelInfoEntity;
import com.ytl.crm.domain.entity.channel.StaffChannelCodeEntity;
import com.ytl.crm.domain.entity.channel.StaffPlatformAccountEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.enums.channel.StatusEnum;
import com.ytl.crm.domain.enums.wechat.QrCodeApplyTypeEnum;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.logic.channel.interfaces.IChannelStaffLogic;
import com.ytl.crm.logic.wechat.interfaces.IWechatQrCodeLogic;
import com.ytl.crm.service.interfaces.channel.IChannelCategoryTreeService;
import com.ytl.crm.service.interfaces.channel.IChannelInfoService;
import com.ytl.crm.service.interfaces.channel.IStaffChannelCodeService;
import com.ytl.crm.service.interfaces.channel.IStaffPlatformAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alibaba.druid.filter.config.ConfigTools.decrypt;
import static com.alibaba.druid.filter.config.ConfigTools.encrypt;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelStaffLogicImpl implements IChannelStaffLogic {

    private final IStaffChannelCodeService iStaffChannelCodeService;
    private final IStaffPlatformAccountService iStaffPlatformAccountService;
    private final IChannelCategoryTreeService iChannelCategoryTreeService;
    private final IChannelInfoService channelInfoService;
    private final IWechatQrCodeLogic iWechatQrCodeLogic;
    private final ChannelQrCodeApolloConfig channelQrCodeApolloConfig;


    @Override
    public boolean saveChannelStaff(StaffBaseBo staffBaseBo) {

        //按企微账号校验
        if (StringUtils.isBlank(staffBaseBo.getExternalId())) {
            throw new UgcCrmServiceException("添加员工，必填企微账号！");
        }

        //校验企微账号重复
        if (Objects.isNull(staffBaseBo.getId())) {
            StaffPlatformAccountEntity staffPlatformAccountEntity = iStaffPlatformAccountService.queryByWxId(staffBaseBo.getExternalId());
            if (Objects.nonNull(staffPlatformAccountEntity)) {
                throw new UgcCrmServiceException("添加员工，企微账号重复！");
            }
        }

        //保存
        StaffPlatformAccountEntity staffPlatformAccountEntity = new StaffPlatformAccountEntity();
        BeanUtils.copyProperties(staffBaseBo, staffPlatformAccountEntity);

        //处理手机号加解密
        try {
            if (Objects.nonNull(staffBaseBo.getStaffPhone())) {
                String encryptedData = encrypt(staffBaseBo.getStaffPhone());
                staffPlatformAccountEntity.setStaffPhone(encryptedData);
            }
        } catch (Exception e) {
            log.error("员工手机号，加密失败！", e);
            throw new UgcCrmServiceException("员工手机号，加密失败！");
        }

        return iStaffPlatformAccountService.saveOrUpdate(staffPlatformAccountEntity);
    }

    @Transactional
    @Override
    public void saveStaffChannelCode(StaffPlatformChannelSaveBo staffPlatformChannelBo) {

        //查询改员工是否已添加过当前渠道码
        StaffChannelLiveBo staffChannelLiveBo = new StaffChannelLiveBo();
        staffChannelLiveBo.setChannelCode(staffPlatformChannelBo.getChannelCode());
        staffChannelLiveBo.setExternalUserId(staffPlatformChannelBo.getExternalId());

        StaffChannelCodeEntity existStaffChannelCode = iStaffChannelCodeService.getStaffChannelLiveCode(staffChannelLiveBo);
        if (Objects.nonNull(existStaffChannelCode)) {
            throw new UgcCrmServiceException("该员工，该渠道，渠道码已存在！");
        }

        ChannelInfoEntity channelInfoEntity = channelInfoService.getById(staffPlatformChannelBo.getChannelCode());
        if (Objects.nonNull(channelInfoEntity) && StatusEnum.ENABLE.getCode() == channelInfoEntity.getStatus()) {

            StaffChannelCodeEntity staffChannelCodeEntity = new StaffChannelCodeEntity();
            BeanUtils.copyProperties(staffPlatformChannelBo, staffChannelCodeEntity);
            staffChannelCodeEntity.setChannelCategoryCode(channelInfoEntity.getCategoryCode());
            //保存渠道二维码
            boolean isOk = iStaffChannelCodeService.save(staffChannelCodeEntity);
            if (isOk) {
//                try {
//                    String fileName = "渠道二维码_".concat(staffChannelCodeEntity.getLogicCode()).concat(".png");
//                    // todo 切换url
//                    //  String qrRouteUrl = channelQrCodeApolloConfig.getChannelQrCodeUrl().concat("?logicCode=").concat(staffChannelCodeEntity.getLogicCode());
//                    String qrRouteUrl = channelQrCodeApolloConfig.getChannelQrCodeUrl();
//                    BufferedImage bufferedImage = QrCodeUtil.getQrCodeImage(qrRouteUrl, 400, 400);
//                    byte[] qrBase64 = QrCodeUtil.convertImageToByteBase64(bufferedImage);
//                    FileInfoResponse fileInfoUrl = storageHelper.uploadFileByBytesWithResp(fileName, qrBase64);
//                    staffChannelCodeEntity.setQrCodeUrl(fileInfoUrl.getUrl());
//                } catch (Exception e) {
//                    throw new UgcCrmServiceException("该员工，该渠道，渠道码上传百度云失败！");
//                }

//                //保存上传URL
//                boolean isQr = iStaffChannelCodeService.saveStaffQrChannelCode(staffChannelCodeEntity);
//                if (!isQr) {
//                    throw new UgcCrmServiceException("该员工，该渠道，渠道码保存百度云链接失败！");
//                }

                //申请二维码
                ChannelQrCodeApplyBO channelQrCodeApplyBO = new ChannelQrCodeApplyBO();
                channelQrCodeApplyBO.setChannelCode(staffChannelCodeEntity.getChannelCode() + "");
                channelQrCodeApplyBO.setUniqueKey(staffChannelCodeEntity.getLogicCode());
                channelQrCodeApplyBO.setEmpWxId(staffChannelCodeEntity.getExternalId());
                channelQrCodeApplyBO.setEmpName(staffPlatformChannelBo.getStaffName());
                channelQrCodeApplyBO.setTypeEnum(QrCodeApplyTypeEnum.CHANNEL);
                log.info("添加渠道码,申请二维码链接:{}", JSON.toJSONString(channelQrCodeApplyBO));
                WechatQrcodeApplyLogEntity applyLog = iWechatQrCodeLogic.applyChannelQrCode(channelQrCodeApplyBO);
                staffChannelCodeEntity.setApplyQrCode(applyLog.getLogicCode());
                boolean isApply = iStaffChannelCodeService.saveStaffApplyChannelCode(staffChannelCodeEntity);
                if (!isApply) {
                    throw new UgcCrmServiceException("该员工，该渠道，渠道码申请二维码失败！");
                }
            } else {
                throw new UgcCrmServiceException("该员工，该渠道，渠道码数据库入库失败！");
            }
        } else {
            throw new UgcCrmServiceException("渠道未启用，不可以添加渠道码！");
        }
    }


    @Override
    public StaffAccountBo getStaffAccountBo(StaffAccountDetailBo staffAccountDetailBo) {

        StaffPlatformAccountEntity staffPlatformAccountEntity = iStaffPlatformAccountService.getStaffAccountBo(staffAccountDetailBo);
        if (Objects.isNull(staffPlatformAccountEntity)) {
            throw new UgcCrmServiceException("该员工不存在");
        }
        StaffAccountBo staffAccountBo = new StaffAccountBo();

        //员工信息
        StaffBaseBo staffBaseBo = new StaffBaseBo();
        BeanUtils.copyProperties(staffPlatformAccountEntity, staffBaseBo);

        try {
            if (Objects.nonNull(staffBaseBo.getStaffPhone())) {
                String encryptedData = decrypt(staffPlatformAccountEntity.getStaffPhone());
                staffBaseBo.setStaffPhone(encryptedData);
            }
        } catch (Exception e) {
            log.error("员工手机号，解密失败！", e);
            throw new UgcCrmServiceException("员工手机号，解密失败！");
        }

        staffAccountBo.setStaffBaseBo(staffBaseBo);

        //员工基本信息
        List<StaffPlatformChannelBo> staffPlatformChannelList = Lists.newArrayList();
        //员工渠道码
        List<StaffChannelCodeEntity> staffChannelCodeList = iStaffChannelCodeService.getStaffChannelLiveCode(staffPlatformAccountEntity.getId());

        if (!CollectionUtils.isEmpty(staffChannelCodeList)) {
            //补全员工渠道信息
            Set<String> categoryCodes = staffChannelCodeList.stream().map(x -> x.getChannelCategoryCode()).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(categoryCodes)) {
                Map<String, String> categoryCodeMaps = iChannelCategoryTreeService.getFulLNameMapByCodes(categoryCodes);
                for (StaffChannelCodeEntity staffChannelCodeEntity : staffChannelCodeList) {
                    StaffPlatformChannelBo staffPlatformChannelBo = new StaffPlatformChannelBo();
                    BeanUtils.copyProperties(staffChannelCodeEntity, staffPlatformChannelBo);
                    //补全渠道分类名称
                    ChannelInfoEntity channelInfoEntity = channelInfoService.getById(staffChannelCodeEntity.getChannelCode());
                    staffPlatformChannelBo.setChannelName(channelInfoEntity.getName());
                    //补全渠道名称
                    staffPlatformChannelBo.setChannelCategoryFullName(categoryCodeMaps.get(staffChannelCodeEntity.getChannelCategoryCode()));
                    staffPlatformChannelList.add(staffPlatformChannelBo);
                }
            }
        }
        staffAccountBo.setStaffPlatformChannelBo(staffPlatformChannelList);
        return staffAccountBo;
    }

    @Override
    public PageResp<StaffBaseBo> getChannelStaff(StaffAccountPageBo staffAccountPageBo) {

        PageResp<StaffBaseBo> pageResp = new PageResp<>();
        PageResp<StaffPlatformAccountEntity> realPageData = iStaffPlatformAccountService.getChannelStaff(staffAccountPageBo);

        List<StaffBaseBo> staffBaseList = Lists.newArrayList();
        pageResp.setTotal(realPageData.getTotal());
        pageResp.setDataList(staffBaseList);

        for (StaffPlatformAccountEntity staffPlatformAccountEntity : realPageData.getDataList()) {
            StaffBaseBo staffBaseBo = new StaffBaseBo();
            BeanUtils.copyProperties(staffPlatformAccountEntity, staffBaseBo);
            try {
                if (Objects.nonNull(staffBaseBo.getStaffPhone())) {
                    String encryptedData = decrypt(staffPlatformAccountEntity.getStaffPhone());
                    staffBaseBo.setStaffPhone(encryptedData);
                }
            } catch (Exception e) {
                log.error("员工手机号，解密失败！", e);
            }
            staffBaseList.add(staffBaseBo);
        }
        return pageResp;
    }

    @Override
    public List<StaffBaseBo> getChannelStaffList(StaffAccountSearchBo staffAccountSearchBo) {
        List<StaffPlatformAccountEntity> list = iStaffPlatformAccountService.getChannelStaffList(staffAccountSearchBo);
        log.info("获取渠道员工-搜索列表:{}", JSON.toJSONString(list));
        List<StaffBaseBo> listVo = Lists.newArrayList();
        for (StaffPlatformAccountEntity staffPlatformAccountEntity : list) {
            StaffBaseBo staffBaseBo = new StaffBaseBo();
            BeanUtils.copyProperties(staffPlatformAccountEntity, staffBaseBo);
            try {
                if (Objects.nonNull(staffBaseBo.getStaffPhone())) {
                    String encryptedData = decrypt(staffPlatformAccountEntity.getStaffPhone());
                    staffBaseBo.setStaffPhone(encryptedData);
                }
            } catch (Exception e) {
                log.error("员工手机号，解密失败！", e);
            }
            listVo.add(staffBaseBo);
        }
        log.info("获取渠道员工-搜索列表:{}", JSON.toJSONString(listVo));
        return listVo;
    }

    @Override
    public StaffChannelCodeEntity getLiveCode(StaffChannelLiveBo staffChannelLiveBo) {
        return iStaffChannelCodeService.getLiveCode(staffChannelLiveBo.getLogicCode());
    }

}