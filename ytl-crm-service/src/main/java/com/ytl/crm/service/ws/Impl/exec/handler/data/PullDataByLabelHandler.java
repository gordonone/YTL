package com.ytl.crm.service.ws.Impl.exec.handler.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskBizInfoService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.service.ws.define.exec.handler.data.IMarketingTaskPullDataHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullDataByLabelHandler implements IMarketingTaskPullDataHandler {


    @Override
    public boolean support(TaskTriggerTypeEnum triggerTypeEnum) {
        return TaskTriggerTypeEnum.LABEL.equals(triggerTypeEnum);
    }

    @Override
    public void pullTaskData(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {

    }


}
