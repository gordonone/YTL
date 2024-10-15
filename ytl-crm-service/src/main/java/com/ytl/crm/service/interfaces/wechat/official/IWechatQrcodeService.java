package com.ytl.crm.service.interfaces.wechat.official;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeEntity;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活码记录 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
public interface IWechatQrcodeService extends IService<WechatQrcodeEntity> {

    WechatQrcodeEntity queryByApplyCodeAndSource(String applyCode, String source);

    WechatQrcodeEntity queryByStateCode(String stateCode, String source);

    List<WechatQrcodeEntity> listExpireQrCode(String source, Date expireTime, Long lastEndId, Integer size);

    boolean deleteEntityById(Long id);

}
