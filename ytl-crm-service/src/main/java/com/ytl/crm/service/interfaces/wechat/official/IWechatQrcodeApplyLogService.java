package com.ytl.crm.service.interfaces.wechat.official;



import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;

import java.util.List;

/**
 * <p>
 * 申请活码记录 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
public interface IWechatQrcodeApplyLogService extends IService<WechatQrcodeApplyLogEntity> {

    WechatQrcodeApplyLogEntity queryByLogicCode(String logicCode);

    List<WechatQrcodeApplyLogEntity> queryApplyCodeByCustomerId(String customerIdType, String customerId);

    List<WechatQrcodeApplyLogEntity> queryExistApplyLog(String channelCode, String uniqueKey, String applyType);

}
