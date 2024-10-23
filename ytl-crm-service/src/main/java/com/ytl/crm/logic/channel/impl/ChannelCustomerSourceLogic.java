package com.ytl.crm.logic.channel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.*;
import com.ytl.crm.domain.entity.common.dict.SysDictEntity;
import com.ytl.crm.domain.enums.channel.ColFormType;
import com.ytl.crm.domain.enums.channel.ColShowTypeEnum;
import com.ytl.crm.domain.enums.channel.DynamicColTypeEnum;
import com.ytl.crm.domain.enums.channel.StatusEnum;
import com.ytl.crm.domain.req.channel.dynamic.DynamicEditReq;
import com.ytl.crm.domain.req.channel.dynamic.DynamicSearchReq;
import com.ytl.crm.domain.resp.channel.ChannelCustomerSourceResp;
import com.ytl.crm.domain.resp.channel.dynamic.DynamicColInfoVo;
import com.ytl.crm.domain.resp.channel.dynamic.DynamicDataVo;
import com.ytl.crm.domain.resp.common.CommonDict;
import com.ytl.crm.service.interfaces.channel.*;
import com.ytl.crm.service.interfaces.dict.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ChannelCustomerSourceLogic {
    @Resource
    private IChannelCustomerSourceService channelCustomerSourceService;
    @Resource
    private IDynamicColInfoService dynamicColInfoService;
    @Resource
    private IDynamicRelateDataService dynamicRelateDataService;
    @Resource
    private IChannelInfoRelateService channelInfoRelateService;

    @Resource
    private IChannelCategoryTreeService channelCategoryTreeService;

    @Resource
    private IChannelInfoService channelInfoService;

    @Resource
    private ISysDictService iSysDictService;
    private static final String suffix = "_dict_code";
    public static final String dynamicTableName = "customer_datasource";

    public BaseResponse<ChannelCustomerSourceResp> search(DynamicSearchReq req) {
        if (req.getCurPage() == null || req.getCurPage() <= 0) {
            req.setCurPage(1);
        }
        if (req.getPageSize() == null || req.getPageSize() <= 0) {
            req.setPageSize(10);
        }
        Page<ChannelCustomerSourceEntity> pageResult = channelCustomerSourceService.getChannelCustomerSourceEntityPage(req);
        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return BaseResponse.responseOk(ChannelCustomerSourceResp.builder().dataList(Collections.emptyList()).total(0L).build());
        }
        //处理基础数据和分类树
        List<ChannelCustomerSourceResp.ChannelCustomerSourceSingle> dataList = handleBaseData(pageResult);
        //根据渠道id查询关联的动态列
        List<Long> colIds = getDynimicColIds(req.getChannelInfoId());
        List<DynamicColInfoVo> dynamicColInfoVos = getDynamicColInfoVos(colIds, true);

        Map<String, List<SysDictEntity>> dictTypeMap = getDynamiceRelateDictMap(dynamicColInfoVos);
        //查询动态列数据
        HandleDynamicRelateDataEntities(dataList, dynamicColInfoVos, dictTypeMap);
        return BaseResponse.responseOk(ChannelCustomerSourceResp.builder().dataList(dataList).dynamicColInfoList(dynamicColInfoVos).total(pageResult.getTotal()).build());
    }

    public BaseResponse<ChannelCustomerSourceResp> editBackShow(Long id, Long channelInfoId) {

        ChannelInfoEntity channelInfoEntity = channelInfoService.getById(channelInfoId);
        List<Long> dynimicColIds = getDynimicColIds(channelInfoId);
        List<DynamicColInfoVo> dynamicColInfoVos = getDynamicColInfoVos(dynimicColIds, false);
        Map<String, List<SysDictEntity>> dynamiceRelateDictMap = getDynamiceRelateDictMap(dynamicColInfoVos);
        List<CommonDict> dictList = getDynamicDicts(dynamiceRelateDictMap);
        addCommonDicts(dictList);


        ChannelCustomerSourceResp.ChannelCustomerSourceSingle single = null;
        if (Objects.nonNull(id)) {
            single = new ChannelCustomerSourceResp.ChannelCustomerSourceSingle();
            ChannelCustomerSourceEntity byId = channelCustomerSourceService.getById(id);
            if (Objects.isNull(byId)) {
                return BaseResponse.responseFail("数据不存在！");
            }
            BeanUtils.copyProperties(byId, single);
            single.setCategoryCode(channelInfoEntity.getCategoryCode());
            HandleDynamicRelateDataEntities(Collections.singletonList(single), dynamicColInfoVos, dynamiceRelateDictMap);
        }
        return BaseResponse.responseOk(ChannelCustomerSourceResp.builder().channelInfoEntity(channelInfoEntity).dynamicColInfoList(dynamicColInfoVos).single(single).dictList(dictList).build());
    }


    //客户动态表单保存
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveOrUpdate(DynamicEditReq req) {

        boolean validate = validate(req);
        if (!validate) {
            return BaseResponse.responseFail("保存失败，已存在一条重复的规则，请检查后再提交");
        }
        //保存或者修改基础数据
        ChannelCustomerSourceEntity channelCustomerSourceEntity = new ChannelCustomerSourceEntity();
        BeanUtils.copyProperties(req, channelCustomerSourceEntity);
        if (Objects.isNull(req.getId())) {
            channelCustomerSourceEntity.setCreateUserCode(req.getUid());
            channelCustomerSourceEntity.setCreateUserName(req.getUserName());
        } else {
            channelCustomerSourceEntity.setModifyUserCode(req.getUid());
            channelCustomerSourceEntity.setModifyUserName(req.getUserName());
            //删除动态数据后新增
            dynamicRelateDataService.removeByDataId(req.getId(), dynamicTableName);
        }
//        List<ChannelCustomerSourceEntity> channelCustomerSourceEntities = channelCustomerSourceService.queryByChannelInfoLogicCode(req.getChannelInfoLogicCode());
//        Long channelInfoId = channelCustomerSourceEntities.get(0).getChannelInfoId();
//        channelCustomerSourceEntity.setChannelInfoId(channelInfoId);
        channelCustomerSourceService.saveOrUpdate(channelCustomerSourceEntity);
        saveDynamicData(req, channelCustomerSourceEntity.getId());
        return BaseResponse.responseOk();
    }

    private boolean validate(DynamicEditReq req) {
        //如果是新增
        //根据渠道code 员工wxId 查询动态数据 逐个匹配看是否存在已有数据
        QueryWrapper<ChannelCustomerSourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelCustomerSourceEntity::getChannelInfoLogicCode, req.getChannelInfoLogicCode()).eq(ChannelCustomerSourceEntity::getEmpWxId, req.getEmpWxId()).ne(Objects.nonNull(req.getId()), ChannelCustomerSourceEntity::getId, req.getId());

        List<ChannelCustomerSourceEntity> channelCustomerSourceEntities = channelCustomerSourceService.list(queryWrapper);
        if (CollectionUtils.isEmpty(channelCustomerSourceEntities)) {
            return true;
        }
        if (CollectionUtils.isEmpty(req.getDynamicData())) {
            return false;
        }

        Map<Long, ChannelCustomerSourceEntity> map = channelCustomerSourceEntities.stream().collect(Collectors.toMap(ChannelCustomerSourceEntity::getId, Function.identity(), (k1, k2) -> k1));
        List<DynamicRelateDataEntity> list = dynamicRelateDataService.list(new QueryWrapper<DynamicRelateDataEntity>().lambda().in(DynamicRelateDataEntity::getDataId, map.keySet()).eq(DynamicRelateDataEntity::getTableName, dynamicTableName));
        Map<Long, List<DynamicRelateDataEntity>> dynamicMap = list.stream().collect(Collectors.groupingBy(DynamicRelateDataEntity::getDataId));
        for (Map.Entry<Long, List<DynamicRelateDataEntity>> entry : dynamicMap.entrySet()) {
            //判断动态列数量 不等的话 下一个
            if (req.getDynamicData().size() != entry.getValue().size()) {
                continue;
            }
            //判断动态列是否一致，一致的话是否值相等
            if (compare(entry.getValue(), req.getDynamicData())) {
                return false;
            }

        }
        return true;
    }

    private boolean compare(List<DynamicRelateDataEntity> dynamicRelateDataEntities, List<DynamicDataVo> dynamicData) {
        boolean result = true;
        //判断动态列是否一致，一致的话是否值相等
        Map<String, DynamicRelateDataEntity> entryValueMap = dynamicRelateDataEntities.stream().collect(Collectors.toMap(DynamicRelateDataEntity::getColName, Function.identity(), (k1, k2) -> k1));
        for (DynamicDataVo dynamicDataVo : dynamicData) {
            DynamicRelateDataEntity dynamicRelateDataEntity = entryValueMap.get(dynamicDataVo.getColName());
            if (Objects.isNull(dynamicRelateDataEntity) || !dynamicRelateDataEntity.getColValue().equals(dynamicDataVo.getColValue())) {
                return false;
            }
        }
        return result;
    }

    //修改状态
    public BaseResponse setStatus(DynamicEditReq req) {
        if (Objects.isNull(req.getId())) {
            return BaseResponse.responseFail("id不能为空！");
        }
        if (Objects.isNull(req.getStatus())) {
            return BaseResponse.responseFail("状态不能为空！");
        }
        ChannelCustomerSourceEntity byId = channelCustomerSourceService.getById(req.getId());
        byId.setStatus(req.getStatus());
        boolean b = channelCustomerSourceService.updateById(byId);
        return BaseResponse.responseOk();
    }

    public List<ChannelCustomerSourceEntity> queryByChannelInfoIdAndDynamicData(String channelLogicCode, Map<String, String> dynamicData) {
        if (CollectionUtils.isEmpty(dynamicData)) {
            return Collections.emptyList();
        }
        List<ChannelCustomerSourceEntity> channelCustomerSourceEntities = channelCustomerSourceService.queryByChannelInfoLogicCode(channelLogicCode);
        if (CollectionUtils.isEmpty(channelCustomerSourceEntities)) {
            return Collections.emptyList();
        }
        List<Long> ids = channelCustomerSourceEntities.stream().map(ChannelCustomerSourceEntity::getId).collect(Collectors.toList());
        List<DynamicRelateDataEntity> dynamicRelateDataEntities = dynamicRelateDataService.list(new QueryWrapper<DynamicRelateDataEntity>().lambda().in(DynamicRelateDataEntity::getDataId, ids).eq(DynamicRelateDataEntity::getTableName, dynamicTableName));

        Map<Long, Map<String, String>> dataIdMaps = dynamicRelateDataEntities.stream().collect(Collectors.groupingBy(DynamicRelateDataEntity::getDataId, Collectors.toMap(DynamicRelateDataEntity::getColName, DynamicRelateDataEntity::getColValue)));
        Set<Long> resultIds = new HashSet<>();
        for (Map.Entry<Long, Map<String, String>> dataIdEntry : dataIdMaps.entrySet()) {
            Map<String, String> colMap = dataIdEntry.getValue();
            boolean flag = true;
            for (Map.Entry<String, String> dynamicEntry : dynamicData.entrySet()) {
                if (!Objects.equals(colMap.get(dynamicEntry.getKey()), dynamicEntry.getValue())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultIds.add(dataIdEntry.getKey());
            }
        }
        if (CollectionUtils.isEmpty(resultIds)) {
            return Collections.emptyList();
        }
        return channelCustomerSourceEntities.stream().filter(e -> resultIds.contains(e.getId())).collect(Collectors.toList());
    }

    private void saveDynamicData(DynamicEditReq req, Long id) {
        if (CollectionUtils.isEmpty(req.getDynamicData())) {
            return;
        }
        List<DynamicRelateDataEntity> saveDatas = req.getDynamicData().stream().map(e -> {
            DynamicRelateDataEntity dynamicRelateDataEntity = new DynamicRelateDataEntity();
            dynamicRelateDataEntity.setDataId(id);
            dynamicRelateDataEntity.setColName(e.getColName());
            dynamicRelateDataEntity.setColValue(e.getColValue());
            dynamicRelateDataEntity.setTableName(dynamicTableName);
            dynamicRelateDataEntity.setCreateUserCode(req.getUid());
            dynamicRelateDataEntity.setModifyUserCode(req.getUid());
            dynamicRelateDataEntity.setCreateUserName(req.getUserName());
            dynamicRelateDataEntity.setModifyUserName(req.getUserName());
            return dynamicRelateDataEntity;
        }).collect(Collectors.toList());
        dynamicRelateDataService.saveBatch(saveDatas);
    }

    private List<CommonDict> getDynamicDicts(Map<String, List<SysDictEntity>> dynamiceRelateDictMap) {
        List<CommonDict> dictList = new ArrayList<>();
        for (Map.Entry<String, List<SysDictEntity>> dictEntry : dynamiceRelateDictMap.entrySet()) {
            CommonDict commonDict = new CommonDict<>(dictEntry.getKey());
            for (SysDictEntity sysDictEntity : dictEntry.getValue()) {
                commonDict.put(sysDictEntity.getCode(), sysDictEntity.getName());
            }
            dictList.add(commonDict);
        }
        return dictList;
    }

    private void addCommonDicts(List<CommonDict> dictList) {
        CommonDict<Integer, String> statusDict = new CommonDict<>("status");
        for (StatusEnum value : StatusEnum.values()) {
            statusDict.put(value.getCode(), value.getDesc());
        }
        dictList.add(statusDict);
    }

    private Map<String, List<SysDictEntity>> getDynamiceRelateDictMap(List<DynamicColInfoVo> dynamicColInfoVos) {
        //查询动态列相关字典信息
        List<String> dictTypeNames = dynamicColInfoVos.stream().filter(e -> !Objects.equals(e.getColFormType(), ColFormType.TEXT_EDIT.getCode())).map(DynamicColInfoVo::getColRelateDictName).collect(Collectors.toList());
        //查询字典信息
        return iSysDictService.MapsByTypes(dictTypeNames);
    }

    private List<Long> getDynimicColIds(Long channelId) {
        List<ChannelInfoRelateEntity> relates = channelInfoRelateService.getByChannelId(channelId);
        return relates.stream().filter(e -> Objects.equals(e.getType(), DynamicColTypeEnum.CUSTOMER_SOURCE_ALLOCATE_RULE.getCode())).map(ChannelInfoRelateEntity::getRelateId).collect(Collectors.toList());
    }

    /**
     * 处理动态表单数据
     *
     * @param dataList          需要处理的数据
     * @param dynamicColInfoVos 动态列信息
     * @param dictTypeMap       动态列关联的字典信息
     */
    private void HandleDynamicRelateDataEntities(List<ChannelCustomerSourceResp.ChannelCustomerSourceSingle> dataList, List<DynamicColInfoVo> dynamicColInfoVos, Map<String, List<SysDictEntity>> dictTypeMap) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        List<Long> dataIds = dataList.stream().map(ChannelCustomerSourceResp.ChannelCustomerSourceSingle::getId).collect(Collectors.toList());
        Map<String, DynamicColInfoVo> dynamicColInfoVoMap = dynamicColInfoVos.stream().collect(Collectors.toMap(DynamicColInfoVo::getColName, e -> e, (v1, v2) -> v1));


        //查询动态数据
        List<DynamicRelateDataEntity> list = dynamicRelateDataService.list(new QueryWrapper<DynamicRelateDataEntity>().lambda().in(DynamicRelateDataEntity::getDataId, dataIds).eq(DynamicRelateDataEntity::getTableName, dynamicTableName));
        Map<Long, List<DynamicRelateDataEntity>> dataIdMap = list.stream().collect(Collectors.groupingBy(DynamicRelateDataEntity::getDataId));
        for (ChannelCustomerSourceResp.ChannelCustomerSourceSingle e : dataList) {
            handleSingleDynamicData(e, dictTypeMap, dataIdMap, dynamicColInfoVoMap);
        }

    }

    private void handleSingleDynamicData(ChannelCustomerSourceResp.ChannelCustomerSourceSingle e, Map<String, List<SysDictEntity>> dictMap, Map<Long, List<DynamicRelateDataEntity>> dataIdMap, Map<String, DynamicColInfoVo> dynamicColInfoVoMap) {
        List<DynamicRelateDataEntity> relateDataList = dataIdMap.get(e.getId());
        if (CollectionUtils.isEmpty(relateDataList)) {
            return;
        }
        List<DynamicDataVo> dynamicDataVos = new ArrayList<>();
        for (DynamicRelateDataEntity dynamicRelateDataEntity : relateDataList) {
            DynamicColInfoVo colInfo = dynamicColInfoVoMap.get(dynamicRelateDataEntity.getColName());
            DynamicDataVo dataVo = new DynamicDataVo();
            dataVo.setColName(dynamicRelateDataEntity.getColName());
            dataVo.setColValue(dynamicRelateDataEntity.getColValue());
            dataVo.setColShowText(dynamicRelateDataEntity.getColValue());
            if (!Objects.equals(colInfo.getColFormType(), ColFormType.TEXT_EDIT.getCode())) {
                //字典类型
                String colShowText = getDictValue(dictMap, colInfo.getColRelateDictName(), dynamicRelateDataEntity.getColValue());
                dataVo.setColShowText(colShowText);
            }
            dynamicDataVos.add(dataVo);
            if (Objects.equals(colInfo.getColListShowType(), ColShowTypeEnum.NAME_CODE.getCode()) && Objects.nonNull(colInfo.getChild())) {
                DynamicDataVo dataVo2 = new DynamicDataVo();
                dataVo2.setColName(colInfo.getChild().getColName());
                dataVo2.setColValue(dynamicRelateDataEntity.getColValue());
                dataVo2.setColShowText(dynamicRelateDataEntity.getColValue());
                dynamicDataVos.add(dataVo2);
            }
        }
        e.setDynamicData(dynamicDataVos);
    }

    private String getDictValue(Map<String, List<SysDictEntity>> dictMap, String dictName, String dictCode) {
        List<SysDictEntity> sysDictEntities = dictMap.get(dictName);
        if (CollectionUtils.isEmpty(sysDictEntities)) {
            return dictCode;
        }
        for (SysDictEntity e : sysDictEntities) {
            if (Objects.equals(e.getCode(), dictCode)) {
                return e.getName();
            }
        }
        return dictCode;
    }

    //根据列ids 查询动态列信息
    private List<DynamicColInfoVo> getDynamicColInfoVos(List<Long> colIds, boolean isListSearch) {
        List<DynamicColInfoEntity> colInfoList = dynamicColInfoService.listByIds(colIds);
        colInfoList.sort(Comparator.comparing(DynamicColInfoEntity::getSort));
        List<DynamicColInfoVo> result = new ArrayList<>();
        for (DynamicColInfoEntity e : colInfoList) {
            DynamicColInfoVo vo1 = DynamicColInfoVo.builder().colDataType(e.getColDataType()).colName(e.getColName()).colFormType(e.getColFormType()).colRelateDictName(e.getColRelateTypeName()).colShowText(e.getColShowNameForValue()).colListShowType(e.getColListShowType()).build();
            result.add(vo1);
            //如果动态列是字典类型，并且需要展示字典code列   则添加一个字典code列
            if (isListSearch && Objects.equals(e.getColListShowType(), ColShowTypeEnum.NAME_CODE.getCode())) {
                DynamicColInfoVo vo2 = DynamicColInfoVo.builder().colDataType(e.getColDataType()).colName(e.getColName() + suffix).colFormType(e.getColFormType()).colRelateDictName(e.getColRelateTypeName()).colShowText(e.getColShowNameForCode()).colListShowType(ColShowTypeEnum.NAME_CODE.getCode()).build();
                vo1.setChild(vo2);
                result.add(vo2);
            }
        }
        return result;
    }

    private List<ChannelCustomerSourceResp.ChannelCustomerSourceSingle> handleBaseData(Page<ChannelCustomerSourceEntity> pageResult) {
        List<ChannelCustomerSourceResp.ChannelCustomerSourceSingle> dataList = new ArrayList<>();
        Set<Long> channelIds = pageResult.getRecords().stream().map(ChannelCustomerSourceEntity::getChannelInfoId).collect(Collectors.toSet());
        List<ChannelInfoEntity> channelInfoEntities = channelInfoService.listByIds(channelIds);
        Map<Long, String> channelIdMap = channelInfoEntities.stream().collect(Collectors.toMap(ChannelInfoEntity::getId, ChannelInfoEntity::getCategoryCode));
        Map<String, String> fulLNameMapByCodes = channelCategoryTreeService.getFulLNameMapByCodes(new HashSet<>(channelIdMap.values()));

        for (ChannelCustomerSourceEntity record : pageResult.getRecords()) {
            ChannelCustomerSourceResp.ChannelCustomerSourceSingle single = new ChannelCustomerSourceResp.ChannelCustomerSourceSingle();
            BeanUtils.copyProperties(record, single);
            single.setCategoryCode(channelIdMap.get(record.getChannelInfoId()));
            single.setCategoryFullName(fulLNameMapByCodes.get(single.getCategoryCode()));
            dataList.add(single);
        }
        return dataList;
    }
}