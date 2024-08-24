package com.ytl.crm.service.ws.define.exec.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 营销任务-关联业务数据 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskBizInfoService extends IService<MarketingTaskBizInfoEntity> {

    MarketingTaskBizInfoEntity queryLastOne(String triggerCode);

    boolean batchSaveBizInfo(List<MarketingTaskBizInfoEntity> entityList);

    List<MarketingTaskBizInfoEntity> queryValidEntity(String triggerCode);

    List<String> queryBizVirtuaKeeperlIdList(String triggerCode);

    /**
     * 根据小乐管家号获取
     */
    List<MarketingTaskBizInfoEntity> queryByVirtualKeeperId(String triggerCode, String virtualKeeperId);

    List<MarketingTaskBizInfoEntity> batchQueryByLogicCode(Collection<String> logicCodes);

    List<String> queryExistBizCode(String triggerCode, Collection<String> bizCodes);

}
