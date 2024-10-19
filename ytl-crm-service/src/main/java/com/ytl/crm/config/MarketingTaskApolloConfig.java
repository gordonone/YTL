package com.ytl.crm.config;

import com.ytl.crm.consumer.dto.resp.data.TaskBizDataDto;
import com.ytl.crm.domain.bo.task.exec.CreateSrOrderConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Data
@Component
public class MarketingTaskApolloConfig {

    /**
     * 项目code和名称，方便随时添加
     */
    @Value("#{${marketingTask.config.projectTypeMap:} ?: {'ACTIVE_SERVICE': '主动服务场景'}}")
    private Map<String, String> projectTypeMap;

    @Value("${marketingTask.exec.pullBizData.size:500}")
    private Integer taskExecPullBizDataSize;


    @Value("${marketingTask.exec.sendMsgTimeLimit:23:00:00}")
    private String sendMsgTimeLimit;


    @Value("${marketingTask.exec.createSrOrder.token:8aa2eda4914583b50191494619160064}")
    private String createSrOrderToken;

    /**
     * 分群id对应的立单数居
     */
    @Value("${marketingTask.exec.createSrOrder.conditionValue.configMap:{}}")
    private Map<String, CreateSrOrderConfig> condiftionValueToConfigMap;


    @Value("${marketingTask.exec.mock.env:[test,qua]}")
    private List<String> taskExecMockEnv;

    @Value("${marketingTask.exec.mock.bizData:[]}")
    private List<TaskBizDataDto> taskMockBizDataList;
}
