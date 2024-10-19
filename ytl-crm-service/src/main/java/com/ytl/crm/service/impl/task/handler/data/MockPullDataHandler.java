package com.ytl.crm.service.impl.task.handler.data;


import com.ytl.crm.consumer.dto.resp.data.TaskBizDataDto;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockPullDataHandler extends AbstractPullDataHandler {


//    @Value("${spring.profiles.active}")
//    private String env;

    @Override
    public void pullTaskData(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
//        //从apollo获取数据
//        List<TaskBizDataDto> bizDataList = marketingTaskApolloConfig.getTaskMockBizDataList();
//        //保存数据
//        super.saveBizData(triggerRecord, bizDataList);
//        //更新状态
//        String triggerCode = triggerRecord.getLogicCode();
//        boolean updateRet = iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
//                TaskTriggerStatusEnum.WAIT_PULL_DATA.getCode(), TaskTriggerStatusEnum.WAIT_CREATE_ACTION.getCode(), null);
//        log.info("更新触发记录状态，triggerCode={}，updateRet={}", triggerCode, updateRet);
    }

    @Override
    public boolean support(TaskTriggerTypeEnum triggerTypeEnum) {
//        List<String> mockEnvList = marketingTaskApolloConfig.getTaskExecMockEnv();
//        boolean envSupportMock = !CollectionUtils.isEmpty(mockEnvList) && mockEnvList.contains(env);
//        return envSupportMock && TaskTriggerTypeEnum.MOCK.equals(triggerTypeEnum);
        //todo 改
        return false;
    }
}
