package com.ytl.crm.service.impl.dict;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.common.dict.SysDictEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.dict.SysDictMapper;
import com.ytl.crm.service.interfaces.dict.ISysDictService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典项配置表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictEntity> implements ISysDictService {

    @Override
    public List<SysDictEntity> listByType(String type) {
        LambdaQueryWrapper<SysDictEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysDictEntity::getType, type);
        wrapper.eq(SysDictEntity::getIsValid, YesOrNoEnum.YES.getCode());
        wrapper.eq(SysDictEntity::getIsDel, YesOrNoEnum.NO.getCode());
        wrapper.orderByDesc(SysDictEntity::getId);
        List<SysDictEntity> entityList = list(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        entityList.sort(Comparator.comparing(SysDictEntity::getSort));
        return entityList;
    }

    @Override
    public Map<String, List<SysDictEntity>> MapsByTypes(List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<SysDictEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SysDictEntity::getType, types);
        wrapper.eq(SysDictEntity::getIsValid, YesOrNoEnum.YES.getCode());
        wrapper.eq(SysDictEntity::getIsDel, YesOrNoEnum.NO.getCode());
        wrapper.orderByDesc(SysDictEntity::getSort);
        List<SysDictEntity> entityList = list(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyMap();
        }
        return entityList.stream().collect(Collectors.groupingBy(SysDictEntity::getType));
    }
}
