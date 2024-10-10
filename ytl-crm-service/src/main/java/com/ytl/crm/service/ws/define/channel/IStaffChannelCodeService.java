package com.ytl.crm.service.ws.define.channel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.bo.channel.StaffChannelLiveBo;
import com.ytl.crm.domain.entity.channel.StaffChannelCodeEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-09-26
 */
public interface IStaffChannelCodeService extends IService<StaffChannelCodeEntity> {


    List<StaffChannelCodeEntity> getStaffChannelLiveCode(Long staffId);

    StaffChannelCodeEntity getLiveCode(String logicCode);

    StaffChannelCodeEntity getStaffChannelLiveCode(StaffChannelLiveBo staffChannelLiveBo);

    boolean saveStaffApplyChannelCode(StaffChannelCodeEntity staffChannelCodeEntity);

    boolean saveStaffQrChannelCode(StaffChannelCodeEntity staffChannelCodeEntity);
}
