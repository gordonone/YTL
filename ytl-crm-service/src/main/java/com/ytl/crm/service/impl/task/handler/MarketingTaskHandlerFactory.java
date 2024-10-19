package com.ytl.crm.service.impl.task.handler;



import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskActionBO;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.service.impl.task.handler.data.MockPullDataHandler;
import com.ytl.crm.service.interfaces.task.exec.handler.action.IMarketingTaskActionExecHandler;
import com.ytl.crm.service.interfaces.task.exec.handler.data.IMarketingTaskPullDataHandler;
import com.ytl.crm.utils.EnumQueryUtil;
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

    @Resource
    private MockPullDataHandler mockPullDataHandler;
    @Resource
    private MarketingTaskApolloConfig marketingTaskApolloConfig;

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

        TaskTriggerTypeEnum triggerTypeEnum =  EnumQueryUtil.of(TaskTriggerTypeEnum.class).getByCode(task.getTriggerType());
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
