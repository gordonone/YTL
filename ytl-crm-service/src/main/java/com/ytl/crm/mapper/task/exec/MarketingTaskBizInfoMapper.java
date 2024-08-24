package com.ytl.crm.mapper.task.exec;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 营销任务-关联业务数据 Mapper 接口
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface MarketingTaskBizInfoMapper extends BaseMapper<MarketingTaskBizInfoEntity> {

    List<String> selectBizVirtualKeeperId(@Param("triggerCode") String triggerCode);

    List<String> selectExistBizCode(@Param("triggerCode") String triggerCode,
                                    @Param("bizCodes") Collection<String> bizCodes);

}
