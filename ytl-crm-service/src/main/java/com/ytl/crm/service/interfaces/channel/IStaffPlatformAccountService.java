package com.ytl.crm.service.interfaces.channel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.bo.channel.StaffAccountDetailBo;
import com.ytl.crm.domain.bo.channel.StaffAccountPageBo;
import com.ytl.crm.domain.bo.channel.StaffAccountSearchBo;
import com.ytl.crm.domain.entity.channel.StaffPlatformAccountEntity;
import com.ytl.crm.domain.resp.common.PageResp;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-09-26
 */
public interface IStaffPlatformAccountService extends IService<StaffPlatformAccountEntity> {


    StaffPlatformAccountEntity getStaffAccountBo(StaffAccountDetailBo staffAccountDetailBo);

    //分页查询任务
    PageResp<StaffPlatformAccountEntity> getChannelStaff(StaffAccountPageBo staffAccountPageBo);

    //查询任务
    List<StaffPlatformAccountEntity> getChannelStaffList(StaffAccountSearchBo staffAccountSearchBo);

    //用企微账号查询员工
    StaffPlatformAccountEntity queryByWxId(String wxId);
}
