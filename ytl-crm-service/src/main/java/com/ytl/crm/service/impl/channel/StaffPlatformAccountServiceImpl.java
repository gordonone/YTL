package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.bo.channel.StaffAccountDetailBo;
import com.ytl.crm.domain.bo.channel.StaffAccountPageBo;
import com.ytl.crm.domain.bo.channel.StaffAccountSearchBo;
import com.ytl.crm.domain.entity.channel.StaffPlatformAccountEntity;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.mapper.channel.StaffPlatformAccountMapper;
import com.ytl.crm.service.interfaces.channel.IStaffPlatformAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class StaffPlatformAccountServiceImpl extends ServiceImpl<StaffPlatformAccountMapper, StaffPlatformAccountEntity> implements IStaffPlatformAccountService {

    @Override
    public StaffPlatformAccountEntity getStaffAccountBo(StaffAccountDetailBo staffAccountDetailBo) {
        return baseMapper.selectById(staffAccountDetailBo.getId());
    }

    @Override
    public PageResp<StaffPlatformAccountEntity> getChannelStaff(StaffAccountPageBo staffAccountPageBo) {
        //todo 其他条件
        LambdaQueryWrapper<StaffPlatformAccountEntity> queryWrapper = Wrappers.lambdaQuery(StaffPlatformAccountEntity.class).select().likeRight(StringUtils.isNotBlank(staffAccountPageBo.getStaffName()), StaffPlatformAccountEntity::getStaffName, staffAccountPageBo.getStaffName()).eq(StringUtils.isNotBlank(staffAccountPageBo.getExternalId()), StaffPlatformAccountEntity::getExternalId, staffAccountPageBo.getExternalId());

        Page<StaffPlatformAccountEntity> page = new Page<>(staffAccountPageBo.getPageNum(), staffAccountPageBo.getPageSize());
        Page<StaffPlatformAccountEntity> pageResult = page(page, queryWrapper);

        if (Objects.isNull(pageResult) || pageResult.getTotal() == 0) {
            return PageResp.emptyResp();
        }
        return PageResp.buildResp(Math.toIntExact(pageResult.getTotal()), pageResult.getRecords());
    }

    @Override
    public List<StaffPlatformAccountEntity> getChannelStaffList(StaffAccountSearchBo staffAccountSearchBo) {

        LambdaQueryWrapper<StaffPlatformAccountEntity> wrapper = Wrappers.lambdaQuery();
        if (staffAccountSearchBo.getStaffName().matches("[\u4E00-\u9FA5]+")) {
            wrapper.likeRight(StaffPlatformAccountEntity::getStaffName, staffAccountSearchBo.getStaffName());
        } else {
            wrapper.likeRight(StaffPlatformAccountEntity::getExternalId, staffAccountSearchBo.getStaffName());
        }
        return list(wrapper);
    }

    @Override
    public StaffPlatformAccountEntity queryByWxId(String wxId) {
        LambdaQueryWrapper<StaffPlatformAccountEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StaffPlatformAccountEntity::getExternalId, wxId);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

}
