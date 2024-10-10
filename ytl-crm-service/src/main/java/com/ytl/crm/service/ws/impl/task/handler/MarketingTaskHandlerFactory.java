package com.ytl.crm.service.ws.impl.task.handler;

import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskActionBO;
import com.ytl.crm.service.ws.define.task.handler.action.IMarketingTaskActionExecHandler;
import com.ytl.crm.service.ws.define.task.handler.data.IMarketingTaskPullDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class MarketingTaskHandlerFactory {

    @Resource
    private List<IMarketingTaskPullDataHandler> pullDataHandlerList;
    @Resource
    private List<IMarketingTaskActionExecHandler> actionExecHandlerList;

    /**
     * 获取拉取任务数据的handler
     *
     * @param task 任务实体
     * @return 处理handler
     */
    public IMarketingTaskPullDataHandler acquirePullDataHandler(MarketingTaskEntity task) {
        if (CollectionUtils.isEmpty(pullDataHandlerList)) {
            log.info("没有拉取业务数据handler的实现类");
            return null;
        }
        String triggerType = task.getTriggerType();
        TaskTriggerTypeEnum triggerTypeEnum = TaskTriggerTypeEnum.valueOf(triggerType);
        IMarketingTaskPullDataHandler retHandler = null;
        for (IMarketingTaskPullDataHandler handler : pullDataHandlerList) {
            if (handler.support(triggerTypeEnum)) {
                retHandler = handler;
                break;
            }
        }
        return retHandler;
    }

    /**
     * 获取执行动作的handler
     *
     * @param actionBO 任务实体
     * @return 处理handler
     */
    public IMarketingTaskActionExecHandler acquireActionExecHandler(MarketingTaskActionBO actionBO) {
        IMarketingTaskActionExecHandler retHandler = null;
        if (!CollectionUtils.isEmpty(actionExecHandlerList)) {
            for (IMarketingTaskActionExecHandler handler : actionExecHandlerList) {
                if (handler.support(actionBO)) {
                    retHandler = handler;
                    break;
                }
            }
        }
        return retHandler;
    }

}
