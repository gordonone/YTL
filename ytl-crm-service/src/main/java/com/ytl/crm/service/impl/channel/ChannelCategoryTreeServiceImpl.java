package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.ChannelCategoryTreeEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.resp.channel.ChannelCategoryNode;
import com.ytl.crm.mapper.channel.ChannelCategoryTreeMapper;
import com.ytl.crm.service.interfaces.channel.IChannelCategoryTreeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Service
public class ChannelCategoryTreeServiceImpl extends ServiceImpl<ChannelCategoryTreeMapper, ChannelCategoryTreeEntity> implements IChannelCategoryTreeService {
    @Override
    public BaseResponse<List<ChannelCategoryNode>> getAll() {
        List<ChannelCategoryNode> nodeList = this.list().stream().filter(e -> Objects.equals(e.getIsDel(), YesOrNoEnum.NO.getCode())).map(e -> ChannelCategoryNode.builder().curNode(e).build()).collect(Collectors.toList());
        Map<Long, List<ChannelCategoryNode>> idMap = nodeList.stream().filter(e -> e.getCurNode().getParentId() != null).collect(Collectors.groupingBy(e -> e.getCurNode().getParentId()));

        //构建树
        List<ChannelCategoryNode> result = new ArrayList<>();
        for (ChannelCategoryNode entity : nodeList) {
            if (ROOT_LEVEL.equals(entity.getCurNode().getLevel())) {
                result.add(entity);
            }
            idMap.computeIfPresent(entity.getCurNode().getId(), (k, v) -> {
                entity.setChildren(v);
                return v;
            });
        }
        return BaseResponse.responseOk(result);
    }

    @Override
    public List<ChannelCategoryTreeEntity> getByLevel(Integer level) {
        QueryWrapper<ChannelCategoryTreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelCategoryTreeEntity::getLevel, level);
        return list(queryWrapper);
    }

    @Override
    public List<ChannelCategoryTreeEntity> getByParentId(Long parentId) {
        QueryWrapper<ChannelCategoryTreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelCategoryTreeEntity::getParentId, parentId).eq(ChannelCategoryTreeEntity::getIsDel, YesOrNoEnum.NO.getCode());
        return list(queryWrapper);
    }

    /**
     * @param categoryCodes
     * @return key:categoryCode value:fullName
     */
    @Override
    public Map<String, String> getFulLNameMapByCodes(Set<String> categoryCodes) {
        Map<String, List<String>> fullCategoryCodeMap = getFullCategoryCodeMap(categoryCodes);
        Set<String> allCategoryCodes = fullCategoryCodeMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        LambdaQueryWrapper<ChannelCategoryTreeEntity> queryWrapper = new QueryWrapper<ChannelCategoryTreeEntity>().lambda().in(ChannelCategoryTreeEntity::getCategoryCode, allCategoryCodes);
        List<ChannelCategoryTreeEntity> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<String, String> map = list.stream().collect(Collectors.toMap(ChannelCategoryTreeEntity::getCategoryCode, ChannelCategoryTreeEntity::getCategoryName, (v1, v2) -> v1));
        Map<String, String> result = new HashMap<>();
        for (String categoryCode : categoryCodes) {
            List<String> fullCategoryCodeList = fullCategoryCodeMap.get(categoryCode);
            StringJoiner joiner = new StringJoiner(SPILT);
            for (String s : fullCategoryCodeList) {
                joiner.add(map.get(s));
            }
            result.put(categoryCode, joiner.toString());
        }
        return result;
    }

    private Map<String, List<String>> getFullCategoryCodeMap(Set<String> categoryCodes) {
        Map<String, List<String>> result = new HashMap<>();
        for (String categoryCode : categoryCodes) {
            if (StringUtils.isBlank(categoryCode)) {
                continue;
            }
            List<String> fullCategoryCodeList = new ArrayList<>();
            String[] splitStr = categoryCode.split(SPILT);
            if (splitStr.length > 0) {
                for (int i = 1; i <= splitStr.length; i++) {
                    StringJoiner joiner = new StringJoiner(SPILT);
                    for (int j = 0; j < i; j++) {
                        joiner.add(splitStr[j]);
                    }
                    fullCategoryCodeList.add(joiner.toString());
                }
            }
            result.put(categoryCode, fullCategoryCodeList);
        }
        return result;
    }

}
