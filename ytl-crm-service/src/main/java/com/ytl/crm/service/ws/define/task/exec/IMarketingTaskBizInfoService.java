package com.ytl.crm.service.ws.define.task.exec;


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

    List<String> queryExistBizCode(String triggerCode, Collection<String> bizCodes);

    List<MarketingTaskBizInfoEntity> listByItemCode(Collection<String> actionItemCode);


}
