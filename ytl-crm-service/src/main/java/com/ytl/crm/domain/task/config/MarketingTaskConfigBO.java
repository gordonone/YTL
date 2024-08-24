package com.ytl.crm.domain.task.config;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MarketingTaskConfigBO {

    /**
     * 任务基本信息
     */
    private MarketingTaskBO taskBaseInfo;

    /**
     * 任务动作信息，按执行顺序排序
     */
    private List<MarketingTaskActionBO> taskActionList;


    public static MarketingTaskConfigBO assemblyBO(MarketingTaskEntity taskEntity,
                                                   List<MarketingTaskActionEntity> actionList,
                                                   List<MarketingTaskActionMaterialEntity> materialList) {
        //1.基本信息
        MarketingTaskConfigBO configBO = new MarketingTaskConfigBO();
        if (taskEntity != null) {
            MarketingTaskBO taskBO = new MarketingTaskBO();
            BeanUtils.copyProperties(taskEntity, taskBO);
            configBO.setTaskBaseInfo(taskBO);
        }

        //2.动作信息
        List<MarketingTaskActionBO> taskActionList = Collections.emptyList();
        //动作素材分组
        Map<String, List<MarketingTaskActionMaterialEntity>> materialMap = !CollectionUtils.isEmpty(materialList) ?
                materialList.stream().collect(Collectors.groupingBy(MarketingTaskActionMaterialEntity::getActionCode))
                : Collections.emptyMap();
        if (!CollectionUtils.isEmpty(actionList)) {
            taskActionList = actionList.stream()
                    .map(item -> MarketingTaskActionBO.assemblyBO(item, materialMap.get(item.getLogicCode())))
                    .collect(Collectors.toList());
        }
        configBO.setTaskActionList(taskActionList);
        return configBO;
    }


    public boolean isConfigOK() {
        return !CollectionUtils.isEmpty(taskActionList);
    }

}
