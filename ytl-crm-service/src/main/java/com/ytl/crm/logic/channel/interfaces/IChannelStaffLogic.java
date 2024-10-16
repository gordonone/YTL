package com.ytl.crm.logic.channel.interfaces;



import com.ytl.crm.domain.bo.channel.*;
import com.ytl.crm.domain.entity.channel.StaffChannelCodeEntity;
import com.ytl.crm.domain.resp.common.PageResp;

import java.util.List;


public interface IChannelStaffLogic {

    //保存员工信息
    boolean saveChannelStaff(StaffBaseBo staffBaseBo);

    //保存渠道码
    boolean saveStaffChannelCode(StaffPlatformChannelSaveBo staffPlatformChannelBo);

    //获取员工详情
    StaffAccountBo getStaffAccountBo(StaffAccountDetailBo staffAccountDetailBo);

    //分页查询员工
    PageResp<StaffBaseBo> getChannelStaff(StaffAccountPageBo staffAccountPageBo);

    //查询员工
    List<StaffBaseBo> getChannelStaffList(StaffAccountSearchBo staffAccountSearchBo);

    //获取码数据
    StaffChannelCodeEntity getLiveCode(StaffChannelLiveBo staffChannelLiveBo);

    //更新申请二维码地址
    boolean saveStaffApplyChannelCode(StaffChannelCodeEntity staffChannelCodeEntity);


}
