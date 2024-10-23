package com.ytl.crm.logic.channel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.ChannelCategoryTreeEntity;
import com.ytl.crm.domain.entity.channel.ChannelOpLogEntity;
import com.ytl.crm.domain.enums.channel.BusinessType;
import com.ytl.crm.domain.enums.channel.OpTypeEnum;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.req.channel.ChannelCategoryAddReq;
import com.ytl.crm.domain.req.channel.ChannelCategoryEditReq;
import com.ytl.crm.domain.req.channel.ChannelCategoryReq;
import com.ytl.crm.domain.resp.channel.ChannelCategoryNode;
import com.ytl.crm.domain.resp.channel.ChannelOpLogsResp;
import com.ytl.crm.domain.resp.common.CommonDict;
import com.ytl.crm.service.interfaces.channel.IChannelCategoryTreeService;
import com.ytl.crm.service.interfaces.channel.IChannelOpLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.ytl.crm.service.interfaces.channel.IChannelCategoryTreeService.SPILT;

@Slf4j
@Component
public class ChannelCategoryLogic {
    @Resource
    private IChannelCategoryTreeService channelCategoryTreeService;
    @Resource
    private IChannelOpLogService channelOpLogService;
//    @Resource
//    private RedissonClient redissonClient;

    private final String add_lock_key = "channel_category_add_lock_key";

    public BaseResponse<List<ChannelCategoryNode>> getAll() {
        return channelCategoryTreeService.getAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<ChannelCategoryTreeEntity> add(ChannelCategoryAddReq req) {
//        RLock addLock = redissonClient.getLock(add_lock_key);
        try {
//            if (!addLock.tryLock()) {
//                return BaseResponse.responseFail("用户正在操作，请稍后再试");
//            }
            if (StringUtils.isBlank(req.getCategoryName())) {
                return BaseResponse.responseFail("分类名称不能为空");
            }
            ChannelCategoryTreeEntity categoryTreeEntity = new ChannelCategoryTreeEntity();
            BeanUtils.copyProperties(req, categoryTreeEntity);
            categoryTreeEntity.setModifyUserCode(req.getUid());
            categoryTreeEntity.setModifyUserName(req.getUserName());
            categoryTreeEntity.setCreateUserCode(req.getUid());
            categoryTreeEntity.setCreateUserName(req.getUserName());
            categoryTreeEntity.setShortCode(1);
            if (Objects.isNull(req.getParentId())) {
                //说明是顶级节点  设置层级和短码
                categoryTreeEntity.setLevel(IChannelCategoryTreeService.ROOT_LEVEL);
                List<ChannelCategoryTreeEntity> byLevel = channelCategoryTreeService.getByLevel(IChannelCategoryTreeService.ROOT_LEVEL);
                byLevel.stream().max(Comparator.comparing(ChannelCategoryTreeEntity::getShortCode)).ifPresent(entity -> categoryTreeEntity.setShortCode(entity.getShortCode() + 1));
                categoryTreeEntity.setCategoryCode("" + categoryTreeEntity.getShortCode());
            } else {
                ChannelCategoryTreeEntity parent = channelCategoryTreeService.getById(req.getParentId());
                categoryTreeEntity.setParentCode(parent.getCategoryCode());
                categoryTreeEntity.setLevel(parent.getLevel() + 1);
                categoryTreeEntity.setShortCode(parent.getChildMaxShortCode() + 1);
                parent.setChildMaxShortCode(categoryTreeEntity.getShortCode());
                categoryTreeEntity.setCategoryCode(parent.getCategoryCode() + SPILT + categoryTreeEntity.getShortCode());
                channelCategoryTreeService.updateById(parent);
            }
            boolean save = channelCategoryTreeService.save(categoryTreeEntity);
            if (save) {
                //保存成功增加日志
                addOpLog(OpTypeEnum.ADD, "名称为：" + categoryTreeEntity.getCategoryName(), categoryTreeEntity.getCategoryCode(), req.getUid(), req.getUserName());
            }
            return save ? BaseResponse.responseOk(categoryTreeEntity) : BaseResponse.responseFail();
        } finally {
//            if (addLock.isHeldByCurrentThread()) {
//                addLock.unlock();
//            }
        }

    }

    public BaseResponse delete(ChannelCategoryReq req) {
        List<ChannelCategoryTreeEntity> byParentId = channelCategoryTreeService.getByParentId(req.getId());
        if (!byParentId.isEmpty()) {
            return BaseResponse.responseFail("该分类下有子分类，请先删除子分类");
        }
        ChannelCategoryTreeEntity channelCategoryTreeEntity = channelCategoryTreeService.getById(req.getId());
        channelCategoryTreeEntity.setIsDel(YesOrNoEnum.YES.getCode());
        channelCategoryTreeEntity.setModifyUserName(req.getUserName());
        channelCategoryTreeEntity.setModifyUserCode(req.getUid());
        channelCategoryTreeEntity.setModifyTime(null);
        boolean b = channelCategoryTreeService.updateById(channelCategoryTreeEntity);
        if (b) {
            addOpLog(OpTypeEnum.DELETE, "名称为：" + channelCategoryTreeEntity.getCategoryName(), channelCategoryTreeEntity.getCategoryCode(), req.getUid(), req.getUserName());
        }
        return b ? BaseResponse.responseOk() : BaseResponse.responseFail();
    }

    public BaseResponse update(ChannelCategoryEditReq req) {


        ChannelCategoryTreeEntity channelCategoryTreeEntity = channelCategoryTreeService.getById(req.getId());
        channelCategoryTreeEntity.setSort(req.getSort());
        channelCategoryTreeEntity.setCategoryName(req.getCategoryName());
        channelCategoryTreeEntity.setRemark(req.getRemark());
        channelCategoryTreeEntity.setModifyTime(null);
        boolean b = channelCategoryTreeService.updateById(channelCategoryTreeEntity);
        if (b) {
            addOpLog(OpTypeEnum.UPDATE, "名称为：" + req.getCategoryName(), channelCategoryTreeEntity.getCategoryCode(), req.getUid(), req.getUserName());
        }
        return b ? BaseResponse.responseOk() : BaseResponse.responseFail();
    }

    public BaseResponse setDisable(ChannelCategoryReq req) {
        ChannelCategoryTreeEntity channelCategoryTreeEntity = channelCategoryTreeService.getById(req.getId());
        channelCategoryTreeEntity.setDisable(req.getDisable());
        channelCategoryTreeEntity.setModifyUserName(req.getUserName());
        channelCategoryTreeEntity.setModifyUserCode(req.getUid());
        boolean b = channelCategoryTreeService.updateById(channelCategoryTreeEntity);
        if (b) {
            addOpLog(Objects.equals(req.getDisable(), YesOrNoEnum.YES.getCode()) ? OpTypeEnum.DISABLE : OpTypeEnum.ENABLE, "名称为：" + channelCategoryTreeEntity.getCategoryName(), channelCategoryTreeEntity.getCategoryCode(), req.getUid(), req.getUserName());
        }
        return b ? BaseResponse.responseOk() : BaseResponse.responseFail();
    }

    public BaseResponse<ChannelOpLogsResp> getByCategoryCode(String categoryCode) {
        QueryWrapper<ChannelOpLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(categoryCode), ChannelOpLogEntity::getOpDataLogicCode, categoryCode).eq(ChannelOpLogEntity::getBusinessType, BusinessType.CHANNEL.getCode()).orderByDesc(ChannelOpLogEntity::getCreateTime);
        List<ChannelOpLogEntity> result = channelOpLogService.list(queryWrapper);
        CommonDict<Object, Object> dict = getOpTypeDict();
        return BaseResponse.responseOk(ChannelOpLogsResp.builder().logs(result).dictList(Arrays.asList(dict)).build());
    }

    private static CommonDict<Object, Object> getOpTypeDict() {
        CommonDict<Object, Object> opTypeDict = new CommonDict<>("opType");
        for (OpTypeEnum value : OpTypeEnum.values()) {
            opTypeDict.put(value.getCode(), value.getDesc());
        }
        return opTypeDict;
    }

    private void addOpLog(OpTypeEnum opTypeEnum, String content, String dataLogicCode, String uid, String userName) {
        ChannelOpLogEntity channelOpLogEntity = new ChannelOpLogEntity();
        channelOpLogEntity.setBusinessType(BusinessType.CHANNEL.getCode());
        channelOpLogEntity.setOpType(opTypeEnum.getCode());
        channelOpLogEntity.setContent(content);
        channelOpLogEntity.setOpUserCode(uid);
        channelOpLogEntity.setOpUserName(userName);
        channelOpLogEntity.setOpDataLogicCode(dataLogicCode);
        channelOpLogService.save(channelOpLogEntity);
    }
}
