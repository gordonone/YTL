package com.ytl.crm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Data
@Component
public class MarketingTaskApolloConfig {

    /**
     * 项目code和名称，方便随时添加
     */
//    @ApolloJsonValue("${marketingTask.config.projectTypeMap:{}}")
//    private Map<String, String> projectTypeMap;

    @Value("${marketingTask.exec.pullBizData.size:500}")
    private Integer taskExecPullBizDataSize;


    @Value("${marketingTask.exec.sendMsgTimeLimit:}")
    private String sendMsgTimeLimit;


    @Value("${marketingTask.exec.createSrOrder.token:8aa2eda4914583b50191494619160064}")
    private String createSrOrderToken;

//    /**
//     * 分群id对应的立单数居
//     */
//    @ApolloJsonValue("${marketingTask.exec.createSrOrder.conditionValue.configMap:{}}")
//    private Map<String, CreateSrOrderConfig> condiftionValueToConfigMap;
//
//
//    @ApolloJsonValue("${marketingTask.exec.mock.env:[]}")
//    private List<String> taskExecMockEnv;
//
//    @ApolloJsonValue("${marketingTask.exec.mock.bizData:[]}")
//    private List<TaskBizDataDto> taskMockBizDataList;
}
