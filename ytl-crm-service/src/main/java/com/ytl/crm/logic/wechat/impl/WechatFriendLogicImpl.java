package com.ytl.crm.logic.wechat.impl;



import com.ytl.crm.consumer.resp.wechat.ExternalContactQueryResp;
import com.ytl.crm.consumer.resp.wechat.ExternalUserContactDTO;
import com.ytl.crm.consumer.resp.wechat.ExternalUserFollowUserDTO;
import com.ytl.crm.consumer.wechat.WxOfficialConsumerHelper;
import com.ytl.crm.domain.bo.wechat.WechatFriendSaveBO;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.resp.wechat.WechatFriendDetailDTO;
import com.ytl.crm.service.interfaces.wechat.official.IWechatFriendRelationService;
import com.ytl.crm.service.interfaces.wechat.official.IWechatQrcodeApplyLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatFriendLogicImpl implements IWechatFriendLogic {

    private final IWechatQrcodeApplyLogService iWechatQrcodeApplyLogService;
    private final IWechatFriendRelationService iWechatFriendRelationService;
    private final WxOfficialConsumerHelper wxOfficialConsumerHelper;

    /**
     * 暂时只能这么实现，目前只能通过这种方式追溯uid
     */
    @Override
    public List<WechatFriendDetailDTO> queryFriendByCustomerId(String customerIdType, String customerId) {
        List<WechatQrcodeApplyLogEntity> applyLogList = iWechatQrcodeApplyLogService.queryApplyCodeByCustomerId(customerIdType, customerId);
        if (Check.isNullOrEmpty((applyLogList))) {
            return Collections.emptyList();
        }
        List<String> applyCodeList = applyLogList.stream().map(WechatQrcodeApplyLogEntity::getLogicCode).collect(Collectors.toList());
        List<WechatFriendRelationEntity> friendList = iWechatFriendRelationService.listByApplyCode(applyCodeList);
        if (Check.isNullOrEmpty((friendList))) {
            return Collections.emptyList();
        }
        return friendList.stream().map(item -> {
            WechatFriendDetailDTO dto = new WechatFriendDetailDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean saveFriend(WechatFriendSaveBO saveBO) {
        String empWxId = saveBO.getEmpWxId();
        String customerWxId = saveBO.getCustomerWxId();
        //保存好友关系 - 这里要不要加锁
        WechatFriendRelationEntity oldRelation = iWechatFriendRelationService.queryByCustomerAndEmpId(customerWxId, empWxId);
        if (oldRelation != null) {
            log.info("关系已存在，无需重复处理，empWxId={}，customerWxId={}", empWxId, customerWxId);
            return true;
        }
        ExternalContactQueryResp contactDetail = wxOfficialConsumerHelper.queryContactDetail(customerWxId);
        WechatFriendRelationEntity entity = buildFriendRelationEntity(saveBO, contactDetail);
        boolean saveRet = iWechatFriendRelationService.save(entity);
        log.info("保存好友关系结果，empWxId={}，customerWxId={}，saveRet={}", empWxId, customerWxId, saveRet);
        return saveRet;
    }

    @Override
    public void delFriend(String empWxId, String customerWxId) {
        boolean delRet = iWechatFriendRelationService.delRelation(empWxId, customerWxId);
        log.info("删除好友，empWxId={}，customerWxId={}，delRet={}", empWxId, customerWxId, delRet);
    }

    private WechatFriendRelationEntity buildFriendRelationEntity(WechatFriendSaveBO saveBO, ExternalContactQueryResp contactDetail) {
        WechatFriendRelationEntity entity = new WechatFriendRelationEntity();
        entity.setApplyCode(saveBO.getApplyCode());
        entity.setCustomerWxId(saveBO.getCustomerWxId());
        entity.setEmpWxId(saveBO.getEmpWxId());
        entity.setAddTime(saveBO.getAddTime());
        entity.setStatus(YesOrNoEnum.NO.getCode());
        if (contactDetail != null) {
            ExternalUserContactDTO contact = contactDetail.getExternalContact();
            entity.setCustomerWxAvatar(contact.getAvatar());
            entity.setCustomerWxName(contact.getName());
            entity.setCustomerWxUnionId(contact.getUnionId());
            entity.setCustomerWxGender(contact.getGender());
            ExternalUserFollowUserDTO followUser = contactDetail.acquireFollowUser(saveBO.getEmpWxId());
            if (followUser != null) {
                if (StringUtils.isNotBlank(followUser.getRemark())) {
                    entity.setCustomerWxRemarkName(followUser.getRemark());
                }
                if (!CollectionUtils.isEmpty(followUser.getRemarkMobiles())) {
                    entity.setCustomerWxRemarkPhone(followUser.getRemarkMobiles().get(0));
                }
            }
        }
        return entity;
    }


    @Override
    public void updateFriend(WechatFriendRelationEntity relation) {
        String customerWxId = relation.getCustomerWxId();
        String empWxId = relation.getEmpWxId();

        //查询对应的客户信息
        ExternalContactQueryResp contactDetail = wxOfficialConsumerHelper.queryContactDetail(customerWxId);
        if (contactDetail == null) {
            log.warn("未查询到外部联系人信息，返回结果");
            return;
        }
        ExternalUserFollowUserDTO followUser = contactDetail.acquireFollowUser(empWxId);
        if (Objects.isNull(followUser)) {
            log.warn("未找到关联的处理人信息");
            return;
        }

        //判断是否需要更新（目前只管手机号和备注，其他暂不处理，手机号目前只处理了一个）
        String newRemark = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(followUser.getRemark())
                && !StringUtils.equalsIgnoreCase(relation.getCustomerWxRemarkName(), followUser.getRemark())) {
            newRemark = followUser.getRemark();
        }
        String newPhone = StringUtils.EMPTY;
        List<String> remarkMobiles = followUser.getRemarkMobiles();
        if (!Check.isNullOrEmpty(remarkMobiles) &&
                !StringUtils.equalsIgnoreCase(relation.getCustomerWxRemarkPhone(), remarkMobiles.get(0))) {
            newPhone = remarkMobiles.get(0);
        }
        if (StringUtils.isAllBlank(newRemark, newPhone)) {
            log.info("手机号和备注均未更新，无需处理");
            return;
        }
        String logicCode = relation.getLogicCode();
        boolean updateRet = iWechatFriendRelationService.updateRemark(logicCode, newRemark, newPhone);
        log.info("更新好友信息，logicCode={}，updateRet={}", logicCode, updateRet);
    }


}
