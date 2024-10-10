package com.ytl.crm.consumer.wechat;

import com.ziroom.ugc.crm.service.web.consumer.dto.req.wechat.*;
import com.ziroom.ugc.crm.service.web.consumer.dto.resp.wechat.*;
import com.ziroom.ugc.footstone.commons.util.Check;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WxOfficialConsumerHelper {

    private final WxOfficialConsumer wxOfficialConsumer;
    private final WxOfficialTokenHelper wxOfficialTokenHelper;

    /**
     * 创建企微二维码
     *
     * @param userId    用户uid
     * @param stateCode state
     * @return 二维码信息
     */
    public WeChatQrCodeDTO createQrCode(String userId, String stateCode) {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        WechatCreateQrCodeReq createReq = new WechatCreateQrCodeReq();
        createReq.setType(1);
        createReq.setUser(Collections.singletonList(userId));
        createReq.setState(stateCode);
        WeChatQrCodeDTO resp = wxOfficialConsumer.createEmpQrCode(accessToken, createReq);
        WxOfficialRespCheckUtil.checkResp(resp);
        return resp;
    }

    /**
     * 删除二维码
     *
     * @param configId 配置id
     * @return 返回失败成功
     */
    public boolean deleteQrCode(String configId) {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        WechatDeleteQrCodeReq deleteReq = new WechatDeleteQrCodeReq();
        deleteReq.setConfig_id(configId);
        WeChatBaseResp deleteResp = wxOfficialConsumer.deleteEmpQrCode(accessToken, deleteReq);
        WxOfficialRespCheckUtil.checkResp(deleteResp);
        return deleteResp.isSuccess();
    }

    public ExternalContactQueryResp queryContactDetail(String customerWxId) {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        ExternalContactQueryResp queryResp = wxOfficialConsumer.getExternalContact(accessToken, customerWxId);
        WxOfficialRespCheckUtil.checkResp(queryResp);
        return queryResp;
    }

    public Map<String, String> batchQueryEmpOfficialId(List<String> thirdIdList, String agentId) {
        Map<String, String> retMap = Collections.emptyMap();
        if (Check.isNullOrEmpty(thirdIdList)) {
            return retMap;
        }
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        ThirdEmpIdConvertReq convertReq = new ThirdEmpIdConvertReq();
        convertReq.setSourceAgentId(agentId);
        convertReq.setOpenUserIdList(thirdIdList);
        ThirdEmpIdConvertResp convertResp = wxOfficialConsumer.thirdEmpIdConvert(accessToken, convertReq);
        WxOfficialRespCheckUtil.checkResp(convertResp);
        if (!Check.isNullOrEmpty(convertResp.getUseridList())) {
            List<ThirdEmpIdConvertResp.UserIdInfo> userIdInfoList = convertResp.getUseridList();
            retMap = userIdInfoList.stream().collect(Collectors.toMap(ThirdEmpIdConvertResp.UserIdInfo::getOpenUserId,
                    ThirdEmpIdConvertResp.UserIdInfo::getUserId, (k1, k2) -> k1));
        }
        return retMap;
    }

    public String queryEmpOfficialId(String empThirdWxId, String agentId) {
        if (StringUtils.isBlank(empThirdWxId)) {
            return StringUtils.EMPTY;
        }
        Map<String, String> idMap = batchQueryEmpOfficialId(Collections.singletonList(empThirdWxId), agentId);
        return idMap.get(empThirdWxId);
    }

    public String queryCustomerOfficialId(String customerThirdWxId, String agentId) {
        if (StringUtils.isBlank(customerThirdWxId)) {
            return StringUtils.EMPTY;
        }
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        ThirdCustomerIdConvertReq convertReq = new ThirdCustomerIdConvertReq();
        convertReq.setExternalUserId(customerThirdWxId);
        convertReq.setSourceAgentId(agentId);
        ThirdCustomerIdConvertResp convertResp = wxOfficialConsumer.thirdCustomerConvert(accessToken, convertReq);
        WxOfficialRespCheckUtil.checkResp(convertResp);
        return convertResp.getExternalUserid();
    }

}
