package com.ytl.crm.service.ws.logic;

import com.alibaba.fastjson.JSON;
import com.ytl.crm.consumer.WorkWeChatConsumer;
import com.ytl.crm.domain.enums.task.config.TaskActionMaterialSendTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionMaterialTypeEnum;
import com.ytl.crm.domain.req.work.ConvertExternalUserIdReq;
import com.ytl.crm.domain.req.ws.WsCorpCreateMsgTaskReq;
import com.ytl.crm.domain.req.ws.WsYuanCustomerReq;
import com.ytl.crm.domain.resp.work.WorkWechatExternalUserIdResp;
import com.ytl.crm.domain.resp.ws.*;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.domain.task.config.MarketingTaskActionMaterialBO;
import com.ytl.crm.help.WorkWechatConsumerHelper;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Component
public class VirtualActualConvertLogicService {

    @Resource
    private WsConsumer wsConsumer;
    @Resource
    private WsConsumerHelper wsConsumerHelper;
    @Resource
    private WorkWeChatConsumer workWeChatConsumer;
    @Resource
    private WorkWechatConsumerHelper workWechatConsumerHelper;
    @Value("${workWeChatAgentId:1000011}")
    private Integer workWeChatAgentId;




    /**
     * 批量从微盛同步自如员工的第三方id列表
     */
    public void batchSyncUpdateUserList() {
        boolean flag = true;
        int currentIndex = 1;
        int pageSize = 100;
        while (flag) {
            WsBaseResponse<WsEmpDetailResp> applyResult = wsConsumer.getUserList(wsConsumerHelper.acquireAccessToken(), currentIndex, pageSize);
            log.info("获取威盛员工列表出参：{}", applyResult);
            WsEmpDetailResp wsEmpDetailResp = ResponseUtils.parseWsBaseResponse(applyResult, "获取威盛员工列表失败");
            int total = wsEmpDetailResp.getTotal();
            log.info("获取威盛员工列表总数：{}", total);
            List<WsEmpDetailResp.WsUserDetail> records = wsEmpDetailResp.getRecords();

            if (CollectionUtils.isEmpty(records) || records.size() < pageSize) {
                flag = false;
            }
            if (!CollectionUtils.isEmpty(records)) {

                for (WsEmpDetailResp.WsUserDetail record : records) {
                    WsYuanCustomerReq wsYuanCustomerReq = new WsYuanCustomerReq();
                    wsYuanCustomerReq.setUser_id(record.getUserId());
                    WsBaseResponse<WsPageResult<WsYuanCustomerResp>> pagesDatas = wsConsumer.queryUserCustomers(wsConsumerHelper.acquireAccessToken(), wsYuanCustomerReq);

                    if (pagesDatas.isOk()) {
                        if (pagesDatas.getData() != null) {
                            List<WsYuanCustomerResp> recordss = pagesDatas.getData().getRecords();
                            System.out.println(recordss);

                            //客户列表
                            List<String> uids = pagesDatas.getData().getRecords().stream().map(x -> x.getExternal_userid()).collect(Collectors.toList());
                            //获取原始的微信标识
                        //    String originalUserExternalId = convertUserExternal(changeEvent.getExternalUserId());

                            MarketingTaskActionMaterialBO marketingTaskActionMaterialBO = new MarketingTaskActionMaterialBO();
                            marketingTaskActionMaterialBO.setMaterialType(TaskActionMaterialTypeEnum.TEXT.getCode());
                            marketingTaskActionMaterialBO.setMaterialContent("私发测测测");

                            WsCorpCreateMsgTaskReq wsCorpCreateMsgTaskReq = buildCreatMsgSendTaskReqForCustomer(record.getUserId(), marketingTaskActionMaterialBO, uids);
                            WsBaseResponse<WsCorpCreateMsgTaskResp> pp = wsConsumer.corpCreateMsgTask(wsConsumerHelper.acquireAccessToken(), wsCorpCreateMsgTaskReq);

                            System.out.println(pp.getData()+"12");
                        }
                        continue;
                    }
                }
            }

            currentIndex++;
        }

    }


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
        return userIdResp.getExternalUserId();
    }

    public WsCorpCreateMsgTaskReq buildCreatMsgSendTaskReqForCustomer(String user_id,
                                                                      MarketingTaskActionMaterialBO materialBO,
                                                                      List<String> customerThirdIdList) {
        WsCorpCreateMsgTaskReq createReq = new WsCorpCreateMsgTaskReq();
        createReq.setCreate_user_id(user_id);
        // 群发任务类型， 10:群发客户-员工一键发送（原企业群发客户）；
        createReq.setType(10);
        // 筛选条件扩展类型，1-员工与部门,2-员工与客户 或 员工与客户群.默认值:1
        createReq.setRange_filter_extra_type(2);
        // 自动提醒时间配置 (1代表15分钟，2代表30分钟，3代表1小时，4代表2小时。默认为2)
        createReq.setTime_config(2);
        createReq.setSend_self(1);
        //任务结束时间
        // String sendMsgTimeLimit = marketingTaskApolloConfig.getSendMsgTimeLimit();
        // Date todayTimeLimit = DateTimeUtil.getTodayTimeLimit(sendMsgTimeLimit, DateTimeUtil.DateTimeUtilFormat.HH_mm_ss.getFormat());
        createReq.setTask_end_time(1724767794l);

        //素材相关
        String materialType = materialBO.getMaterialType();
        if (TaskActionMaterialTypeEnum.TEXT.equalsCode(materialType)
                && StringUtils.isBlank(materialBO.getMaterialId())) {
            //文本 && 未设置素材id
            createReq.setText_content(materialBO.getMaterialContent());
        } else {
            TaskActionMaterialSendTypeEnum sendTypeEnum = TaskActionMaterialSendTypeEnum.valueOf(materialBO.getSendType());
            WsCorpCreateMsgTaskReq.MaterialSendDto materialSendDto = new WsCorpCreateMsgTaskReq.MaterialSendDto();
            materialSendDto.setMaterial_id(Long.parseLong(materialBO.getMaterialId()));
            materialSendDto.setSend_type(sendTypeEnum.getWsSendType());
            createReq.setMaterial_send_dto(Collections.singletonList(materialSendDto));
        }

        //extra
        WsCorpCreateMsgTaskReq.ExtraParam_10 extraParam = new WsCorpCreateMsgTaskReq.ExtraParam_10();
        extraParam.setUserid(user_id);
        extraParam.setExternal_userid(customerThirdIdList);
        WsCorpCreateMsgTaskReq.Extra<WsCorpCreateMsgTaskReq.ExtraParam_10> extraInfo = new WsCorpCreateMsgTaskReq.Extra<>();
        extraInfo.setParams(Collections.singletonList(extraParam));

        createReq.setExtra(JSON.toJSONString(extraInfo));

        return createReq;
    }


}
