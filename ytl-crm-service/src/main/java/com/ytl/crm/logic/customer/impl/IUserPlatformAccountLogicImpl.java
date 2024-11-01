package com.ytl.crm.logic.customer.impl;


import com.alibaba.fastjson.JSON;
import com.ytl.crm.domain.bo.wechat.WechatFriendSaveBO;
import com.ytl.crm.domain.entity.customer.UserPlatformAccountEntity;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.domain.enums.wechat.WechatFriendStatusEnum;
import com.ytl.crm.logic.customer.interfaces.IUserPlatformAccountLogic;
import com.ytl.crm.service.interfaces.customer.IUserPlatformAccountService;
import com.ytl.crm.service.interfaces.wechat.official.IWechatFriendRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class IUserPlatformAccountLogicImpl implements IUserPlatformAccountLogic {

    private final IUserPlatformAccountService iUserPlatformAccountService;
    private final IWechatFriendRelationService iWechatFriendRelationService;


    @Override
    public void saveUserPlatformAccount(WechatFriendSaveBO saveBO) {

        //历史好友关系
        String customerWxId = saveBO.getCustomerWxId();
        String empWxId = saveBO.getEmpWxId();
        WechatFriendRelationEntity oldRelation = iWechatFriendRelationService.queryByCustomerAndEmpId(customerWxId, empWxId);

        UserPlatformAccountEntity userPlatformAccountEntity = iUserPlatformAccountService.queryByCustomerAndEmpId(customerWxId, empWxId);
        if (Objects.nonNull(oldRelation)) {

            UserPlatformAccountEntity userPlatformAccountNewEntity = buildUserBaseEntity(userPlatformAccountEntity, oldRelation);
            log.info("补充客户信息,原始客户关系:{},新客户信息:{}", JSON.toJSONString(oldRelation), JSON.toJSONString(userPlatformAccountEntity));

            if (Objects.nonNull(userPlatformAccountNewEntity.getId())) {
                iUserPlatformAccountService.updateById(userPlatformAccountNewEntity);
            } else {
                iUserPlatformAccountService.save(userPlatformAccountNewEntity);
            }
        } else {
            //标记客户信息删除
            if (Objects.nonNull(userPlatformAccountEntity) && Objects.equals(userPlatformAccountEntity.getStatus(), WechatFriendStatusEnum.ADDED.getCode())) {

                userPlatformAccountEntity.setStatus(WechatFriendStatusEnum.DELETED.getCode());
                //标记客户删除
                log.info("补充客户信息,删除好友关系，原始客户数据:{}", JSON.toJSONString(userPlatformAccountEntity));
                iUserPlatformAccountService.updateById(userPlatformAccountEntity);
            }
        }
    }

    private UserPlatformAccountEntity buildUserBaseEntity(UserPlatformAccountEntity userPlatformAccountEntity, WechatFriendRelationEntity oldRelation) {
        UserPlatformAccountEntity entity = new UserPlatformAccountEntity();

        if (Objects.nonNull(userPlatformAccountEntity)) {
            entity.setId(userPlatformAccountEntity.getId());
            entity.setLogicCode(userPlatformAccountEntity.getLogicCode());
        }

        entity.setCustomerWxId(oldRelation.getCustomerWxId());
        entity.setEmpWxId(oldRelation.getEmpWxId());
        entity.setAddTime(oldRelation.getAddTime());
        entity.setStatus(oldRelation.getStatus());


        if (StringUtils.isNotBlank(oldRelation.getCustomerWxAvatar())) {
            entity.setCustomerWxAvatar(oldRelation.getCustomerWxAvatar());
        }

        if (Objects.nonNull(oldRelation.getCustomerWxGender())) {
            entity.setCustomerWxGender(oldRelation.getCustomerWxGender());
        }

        if (StringUtils.isNotBlank(oldRelation.getCustomerWxName())) {
            entity.setCustomerWxName(oldRelation.getCustomerWxName());
        }

        if (StringUtils.isNotBlank(oldRelation.getCustomerWxUnionId())) {
            entity.setCustomerWxUnionId(oldRelation.getCustomerWxUnionId());
        }

        if (StringUtils.isNotBlank(oldRelation.getCustomerWxRemarkPhone())) {
            entity.setCustomerWxRemarkPhone(oldRelation.getCustomerWxRemarkPhone());
        }

        if (StringUtils.isNotBlank(oldRelation.getCustomerWxRemarkName())) {
            entity.setCustomerWxRemarkName(oldRelation.getCustomerWxRemarkName());
        }

        return entity;
    }
}

