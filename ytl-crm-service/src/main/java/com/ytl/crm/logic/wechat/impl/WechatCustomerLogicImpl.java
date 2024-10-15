package com.ytl.crm.logic.wechat.impl;


import com.ytl.crm.config.WeChatThirdSourceConfig;
import com.ytl.crm.consumer.wechat.WxOfficialConsumerHelper;
import com.ytl.crm.domain.entity.wechat.WechatCustomerMappingEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import com.ytl.crm.service.interfaces.wechat.official.IWechatCustomerMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatCustomerLogicImpl implements IWechatCustomerLogic {

    private final WeChatThirdSourceConfig weChatThirdSourceConfig;
    private final IWechatCustomerMappingService iWechatCustomerMappingService;

    private final WxOfficialConsumerHelper wxOfficialConsumerHelper;

    @Override
    public String transferToOfficialId(String customerThirdWxId, WechatSourceEnum sourceEnum) {
        WechatCustomerMappingEntity mappingEntity = iWechatCustomerMappingService.queryByThirdId(customerThirdWxId, sourceEnum.getCode());
        if (mappingEntity != null && StringUtils.isNotBlank(mappingEntity.getCustomerWxId())) {
            return mappingEntity.getCustomerThirdWxId();
        }
        String agentId = weChatThirdSourceConfig.acquireThirdSourceAgentId(sourceEnum);
        if (StringUtils.isBlank(agentId)) {
            log.warn("未配置对应微信来源的agentId，source={}", sourceEnum.getCode());
            return null;
        }
        String customerOfficialId = wxOfficialConsumerHelper.queryCustomerOfficialId(customerThirdWxId, agentId);
        //处理转换后置逻辑，可以异步或者try-catch，不影响主流程
        logicAfterTransfer(mappingEntity, customerThirdWxId, customerOfficialId, sourceEnum);
        return customerOfficialId;
    }


    private void logicAfterTransfer(WechatCustomerMappingEntity oldEntity,
                                    String empThirdWxId, String officialWxId,
                                    WechatSourceEnum sourceEnum) {
        if (StringUtils.isBlank(officialWxId)) {
            return;
        }
        if (oldEntity != null) {
            boolean updateRet = iWechatCustomerMappingService.updateCustomerWxIdById(oldEntity.getId(), officialWxId);
            log.info("更新客户官方微信id，结果updateRet={}，empThirdWxId={}，officialEmpId={}", updateRet, empThirdWxId, officialWxId);
        } else {
            WechatCustomerMappingEntity newEntity = buildEmpMappingEntity(empThirdWxId, officialWxId, sourceEnum);
            boolean saveRet = iWechatCustomerMappingService.save(newEntity);
            log.info("保存客户官方微信id映射，结果updateRet={}，empThirdWxId={}，officialEmpId={}", saveRet, empThirdWxId, officialWxId);
        }
    }

    private WechatCustomerMappingEntity buildEmpMappingEntity(String thirdWxId, String officialWxId, WechatSourceEnum sourceEnum) {
        WechatCustomerMappingEntity entity = new WechatCustomerMappingEntity();
        entity.setIsDelete(YesOrNoEnum.NO.getCode());
        entity.setCustomerWxId(officialWxId);
        entity.setCustomerThirdWxId(thirdWxId);
        entity.setThirdSource(sourceEnum.getCode());
        return entity;
    }


}
