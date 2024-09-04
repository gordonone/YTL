package com.ytl.crm.service.ws.define.friend;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.friend.WechatEmpMappingEntity;

import java.util.List;

/**
 * <p>
 * 员工标识映射表 服务类
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
public interface IWechatEmpMappingService extends IService<WechatEmpMappingEntity> {

    String getEmpIdByEmpThirdId(String empThirdId);


    String getEmpThirdIdByEmpId(String empId);

    List<WechatEmpMappingEntity> getEmpByEmpThirdId(List<String> empThirdIds);

}
