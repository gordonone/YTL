package com.ytl.crm.service.impl.task.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.task.exec.MarketingTaskBizInfoMapper;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskBizInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 营销任务-关联业务数据 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskBizInfoServiceImpl extends ServiceImpl<MarketingTaskBizInfoMapper, MarketingTaskBizInfoEntity> implements IMarketingTaskBizInfoService {

    private final MarketingTaskBizInfoMapper marketingTaskBizInfoMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public MarketingTaskBizInfoEntity queryLastOne(String triggerCode) {
        LambdaQueryWrapper<MarketingTaskBizInfoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskBizInfoEntity::getTriggerRecordCode, triggerCode);
        wrapper.orderByDesc(MarketingTaskBizInfoEntity::getId);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public boolean batchSaveBizInfo(List<MarketingTaskBizInfoEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return true;
        }
        if (entityList.size() == 1) {
            MarketingTaskBizInfoEntity bizInfoEntity = entityList.get(0);
            return save(bizInfoEntity);
        }

        int result = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            MarketingTaskBizInfoMapper mapper = sqlSession.getMapper(MarketingTaskBizInfoMapper.class);
            //每1000条提交一次
            int batchCount = 1000;
            for (int index = 0; index < entityList.size(); index++) {
                MarketingTaskBizInfoEntity bizInfoEntity = entityList.get(index);
                mapper.insert(bizInfoEntity);
                if (index != 0 && index % batchCount == 0) {
                    sqlSession.commit();
                }
                log.info("批量保存任务业务数据进度，index={}", index);
            }
            sqlSession.commit();
            sqlSession.clearCache();
            result = entityList.size();
        } catch (Exception e) {
            sqlSession.rollback();
            result = -1;
            log.error("批量保存任务业务数据异常回滚", e);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return result != entityList.size();
    }

    @Override
    public List<MarketingTaskBizInfoEntity> queryValidEntity(String triggerCode) {
        LambdaQueryWrapper<MarketingTaskBizInfoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskBizInfoEntity::getTriggerRecordCode, triggerCode);
        wrapper.eq(MarketingTaskBizInfoEntity::getIsValid, YesOrNoEnum.YES.getCode());
        wrapper.orderByDesc(MarketingTaskBizInfoEntity::getId);
        return list(wrapper);
    }

    @Override
    public List<String> queryExistBizCode(String triggerCode, Collection<String> bizCodes) {
        if (StringUtils.isBlank(triggerCode) || CollectionUtils.isEmpty(bizCodes)) {
            return Collections.emptyList();
        }
        return marketingTaskBizInfoMapper.selectExistBizCode(triggerCode, bizCodes);
    }

    @Override
    public List<MarketingTaskBizInfoEntity> listByItemCode(Collection<String> actionItemCodes) {
        if (CollectionUtils.isEmpty(actionItemCodes)) {
            return Collections.emptyList();
        }
        return marketingTaskBizInfoMapper.listByItemCode(actionItemCodes);
    }


}
