package com.ytl.crm.service.ws.logic;

import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsEmpDetailResp;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Component
public class VirtualActualConvertLogicService {

    @Resource
    private WsConsumer wsConsumer;
    @Resource
    private WsConsumerHelper wsConsumerHelper;


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

            }

            currentIndex++;
        }

    }


}
