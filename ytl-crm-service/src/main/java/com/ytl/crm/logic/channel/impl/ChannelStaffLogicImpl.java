package com.ytl.crm.logic.channel.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.ytl.crm.common.exception.UgcCrmServiceException;
import com.ytl.crm.domain.bo.channel.*;
import com.ytl.crm.domain.entity.channel.ChannelInfoEntity;
import com.ytl.crm.domain.entity.channel.StaffChannelCodeEntity;
import com.ytl.crm.domain.entity.channel.StaffPlatformAccountEntity;
import com.ytl.crm.domain.enums.channel.StatusEnum;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.logic.channel.interfaces.IChannelStaffLogic;
import com.ytl.crm.service.interfaces.channel.IChannelCategoryTreeService;
import com.ytl.crm.service.interfaces.channel.IChannelInfoService;
import com.ytl.crm.service.interfaces.channel.IStaffChannelCodeService;
import com.ytl.crm.service.interfaces.channel.IStaffPlatformAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Override
    public boolean saveStaffChannelCode(StaffPlatformChannelSaveBo staffPlatformChannelBo) {

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
                try {
                    String fileName = "渠道二维码_".concat(staffChannelCodeEntity.getLogicCode()).concat(".png");
//                    // todo 切换url
//                    String qrRouteUrl = "?logicCode=".concat(staffChannelCodeEntity.getLogicCode());
//                    BufferedImage bufferedImage = QrCodeUtil.getQrCodeImage("https://www.baidu.com", 400, 400);
//                    byte[] qrBase64 = QrCodeUtil.convertImageToByteBase64(bufferedImage);
//                    FileInfoResponse fileInfoUrl = storageHelper.uploadFileByBytesWithResp(fileName, qrBase64);
//                    staffChannelCodeEntity.setQrCodeUrl(fileInfoUrl.getUrl());
                    return iStaffChannelCodeService.saveStaffQrChannelCode(staffChannelCodeEntity);
                } catch (Exception e) {
                    log.error("生成渠道码图片二维码异常", e);
                }
            }
        } else {
            throw new UgcCrmServiceException("渠道未启用，不可以添加渠道码");
        }
        return false;
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
     //   log.info("获取渠道员工-搜索列表:{}", Json.toJsonString(list));
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
       // log.info("获取渠道员工-搜索列表:{}", JsSO.toJsonString(listVo));
        return listVo;
    }

    @Override
    public StaffChannelCodeEntity getLiveCode(StaffChannelLiveBo staffChannelLiveBo) {
        return iStaffChannelCodeService.getLiveCode(staffChannelLiveBo.getLogicCode());
    }

    @Override
    public boolean saveStaffApplyChannelCode(StaffChannelCodeEntity staffChannelCodeEntity) {
        return iStaffChannelCodeService.saveStaffApplyChannelCode(staffChannelCodeEntity);
    }


}
