package com.ytl.crm.service.ws.define.wechat.official;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.wechat.WechatEmpMappingEntity;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 员工微信映射表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
public interface IWechatEmpMappingService extends IService<WechatEmpMappingEntity> {

    List<WechatEmpMappingEntity> listByThirdId(Collection<String> thirdWxIds, String thirdSource);

    List<WechatEmpMappingEntity> listWaitFillEntity(String thirdSource, Long lastEndId, Integer size);

    boolean updateEmpWxIdById(Long id, String empWxId);

    WechatEmpMappingEntity queryByThirdId(String empThirdWxId, String thirdSource);

}
