package com.ytl.crm.logic.wechat.impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Maps;
import com.ytl.crm.common.exception.UgcCrmServiceException;
import com.ytl.crm.config.wechat.WeChatQrCodeConfig;
import com.ytl.crm.consumer.resp.wechat.WeChatQrCodeDTO;
import com.ytl.crm.consumer.wechat.WxOfficialConsumerHelper;
import com.ytl.crm.domain.bo.wechat.ChannelQrCodeApplyBO;
import com.ytl.crm.domain.bo.wechat.CustomerQrCodeApplyBO;
import com.ytl.crm.domain.bo.wechat.QrCodeApplyBO;
import com.ytl.crm.domain.entity.channel.ChannelCustomerSourceEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeEntity;
import com.ytl.crm.domain.enums.wechat.QrCodeApplyTypeEnum;
import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import com.ytl.crm.domain.req.wechat.CustomerQrCodeGenReq;
import com.ytl.crm.domain.req.wechat.WechatBaseReq;
import com.ytl.crm.domain.resp.wechat.CustomerWeChatQrCodeDTO;
import com.ytl.crm.logic.wechat.interfaces.IWechatQrCodeLogic;
import com.ytl.crm.service.interfaces.wechat.official.IWechatQrcodeApplyLogService;
import com.ytl.crm.service.interfaces.wechat.official.IWechatQrcodeService;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 二维码相关的底层逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatQrCodeLogicImpl implements IWechatQrCodeLogic {

    private final WeChatQrCodeConfig weChatQrCodeConfig;
    private final IWechatQrcodeApplyLogService iWechatQrcodeApplyLogService;
    private final IWechatQrcodeService iWechatQrcodeService;
    private final WxOfficialConsumerHelper wxOfficialConsumerHelper;
    private final ChannelCustomerSourceLogic channelCustomerSourceLogic;

    private final Map<String, Function<WechatQrcodeApplyLogEntity, WechatQrcodeEntity>> GEN_QR_CODE_FUNC_MAP = Maps.newConcurrentMap();

    @PostConstruct
    public void init() {
        GEN_QR_CODE_FUNC_MAP.put(WechatSourceEnum.OFFICIAL.getCode(), this::genWxOfficialQrCode);
    }

    @Override
    public boolean checkTokenValid(WechatBaseReq baseReq) {
        String scene = baseReq.getScene();
        String token = baseReq.getToken();
        if (StringUtils.isAnyBlank(scene, token)) {
            return false;
        }
        return weChatQrCodeConfig.isValidToken(scene, token);
    }

    @Override
    public WechatQrcodeApplyLogEntity applyChannelQrCode(ChannelQrCodeApplyBO applyBO) {
        boolean checkOk = checkApplyBO(applyBO);
        if (!checkOk) {
            throw new UgcCrmServiceException("申请二维码必要参数不能为空");
        }
        //查询已经存在的符合条件的申请记录
        WechatQrcodeApplyLogEntity applyLog = queryExistApplyLog(applyBO);
        if (applyLog == null) {
            applyLog = createChannelApplyLog(applyBO);
        }
        return applyLog;
    }

    @Override
    public WechatQrcodeApplyLogEntity applyCustomerQrCode(CustomerQrCodeApplyBO applyBO) {
        boolean checkOk = checkApplyBO(applyBO);
        if (!checkOk) {
            throw new UgcCrmServiceException("申请二维码必要参数不能为空");
        }
        //查询已经存在的符合条件的申请记录
        WechatQrcodeApplyLogEntity applyLog = queryExistApplyLog(applyBO);
        if (applyLog == null) {
            //重新生成记录
            applyLog = createCustomerApplyLog(applyBO);
        }
        return applyLog;
    }

    private boolean checkApplyBO(QrCodeApplyBO applyBO) {
        if (StringUtils.isAnyBlank(applyBO.getChannelCode(), applyBO.getUniqueKey(),
                applyBO.getEmpWxId(), applyBO.getEmpName())) {
            return false;
        }
        return applyBO.getTypeEnum() != null;
    }

    private WechatQrcodeApplyLogEntity queryExistApplyLog(QrCodeApplyBO applyBO) {
        String channelCode = applyBO.getChannelCode();
        String uniqueKey = applyBO.getUniqueKey();
        String empWxId = applyBO.getEmpWxId();
        QrCodeApplyTypeEnum typeEnum = applyBO.getTypeEnum();
        List<WechatQrcodeApplyLogEntity> existApplyLogs = iWechatQrcodeApplyLogService.queryExistApplyLog(channelCode,
                uniqueKey, typeEnum.getCode());
        if (CollectionUtils.isEmpty(existApplyLogs)) {
            return null;
        }
        //已存在的和待申请的是同一个微信号，这里兼容一客一码修改规则的情况
        return existApplyLogs.stream()
                .filter(item -> StringUtils.equalsIgnoreCase(item.getEmpWxId(), empWxId))
                .findFirst().orElse(null);
    }

    private WechatQrcodeApplyLogEntity createChannelApplyLog(ChannelQrCodeApplyBO applyBO) {
        WechatQrcodeApplyLogEntity applyLog = convertChannelBoToEntity(applyBO);
        boolean saveRet = iWechatQrcodeApplyLogService.save(applyLog);
        if (!saveRet) {
            log.warn("保存渠道码申请记录失败，uniqueKey={}", applyBO.getUniqueKey());
            throw new UgcCrmServiceException("保存渠道码申请记录异常：" + applyBO.getUniqueKey());
        }
        return applyLog;
    }

    private WechatQrcodeApplyLogEntity createCustomerApplyLog(CustomerQrCodeApplyBO applyBO) {
        WechatQrcodeApplyLogEntity entity = convertCustomerBoToEntity(applyBO);
        boolean saveRet = iWechatQrcodeApplyLogService.save(entity);
        if (!saveRet) {
            log.warn("保存客户码申请记录失败，uniqueKey={}", applyBO.getUniqueKey());
            throw new UgcCrmServiceException("保存客户码申请记录异常：" + applyBO.getUniqueKey());
        }
        return entity;
    }

    private WechatQrcodeApplyLogEntity convertChannelBoToEntity(ChannelQrCodeApplyBO bo) {
        return buildApplyEntity(bo);
    }

    private WechatQrcodeApplyLogEntity convertCustomerBoToEntity(CustomerQrCodeApplyBO bo) {
        WechatQrcodeApplyLogEntity entity = buildApplyEntity(bo);
        entity.setBizType(bo.getBizType());
        entity.setBizKey(bo.getBizKey());
        entity.setBizParam(bo.getBizParam());
        entity.setCustomerIdType(bo.getCustomerIdType());
        entity.setCustomerId(bo.getCustomerId());
        return entity;
    }

    private WechatQrcodeApplyLogEntity buildApplyEntity(QrCodeApplyBO bo) {
        QrCodeApplyTypeEnum typeEnum = bo.getTypeEnum();
        WechatQrcodeApplyLogEntity entity = new WechatQrcodeApplyLogEntity();
        entity.setApplyType(typeEnum.getCode());
        entity.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
        entity.setUniqueKey(bo.getUniqueKey());
        entity.setChannelCode(bo.getChannelCode());
        entity.setEmpWxId(bo.getEmpWxId());
        entity.setEmpName(bo.getEmpName());
        return entity;
    }

    @Override
    public CustomerWeChatQrCodeDTO queryQrCodeByApplyCode(String applyCode, String qrCodeSource) {
        //未找到对应的申请记录
        WechatQrcodeApplyLogEntity applyLog = iWechatQrcodeApplyLogService.queryByLogicCode(applyCode);
        if (applyLog == null) {
            log.error("未找到对应的申请记录，记录applyCode={}", applyCode);
            throw new UgcCrmServiceException("未找到对应的申请记录");
        }

        //查已经生成过的记录，过期的is_delete = 1，单独定时任务维护
        WechatQrcodeEntity qrCodeEntity = iWechatQrcodeService.queryByApplyCodeAndSource(applyCode, qrCodeSource);
        if (qrCodeEntity == null) {
            qrCodeEntity = genQrCodeWithLock(applyLog, qrCodeSource);
        }
        return buildQrCodDto(applyLog, qrCodeEntity);
    }

    private WechatQrcodeEntity genQrCodeWithLock(WechatQrcodeApplyLogEntity applyLog, String qrCodeSource) {
        String applyCode = applyLog.getLogicCode();
        WechatQrcodeEntity qrCodeEntity = null;
        //这里加个锁，避免重复调用接口
//        String lockKey = DistributeLockKeyEnum.GEN_QR_CODE.buildKey(qrCodeSource, applyCode);
//        RLock lock = redissonClient.getLock(lockKey);
    //    try {
     //      lock.lock();
            //再检测一一遍，并发请求时有用。。
            qrCodeEntity = iWechatQrcodeService.queryByApplyCodeAndSource(applyCode, qrCodeSource);
            if (qrCodeEntity == null) {
                //生成qrCode
                qrCodeEntity = genQrCode(applyLog, qrCodeSource);
            }
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
        return qrCodeEntity;
    }

    private WechatQrcodeEntity genQrCode(WechatQrcodeApplyLogEntity applyLog, String qrCodeSource) {
        String applyCode = applyLog.getLogicCode();
        Function<WechatQrcodeApplyLogEntity, WechatQrcodeEntity> func = GEN_QR_CODE_FUNC_MAP.get(qrCodeSource);
        if (func == null) {
            log.error("不支持的创建二维码渠道，source={}", qrCodeSource);
            throw new UgcCrmServiceException("不支持的创建二维码渠道，source=" + qrCodeSource);
        }
        WechatQrcodeEntity qrCodeEntity = func.apply(applyLog);
        if (qrCodeEntity == null) {
            log.info("保存qrCode记录异常，applyCode={}", applyCode);
            throw new UgcCrmServiceException("生成微二维码异常");
        }

        //保存记录
        boolean saveRet = iWechatQrcodeService.save(qrCodeEntity);
        if (!saveRet) {
            log.info("保存qrCode记录异常，applyCode={}", applyCode);
            throw new UgcCrmServiceException("保存qrCode记录异常");
        }
        return qrCodeEntity;
    }

    private WechatQrcodeEntity genWxOfficialQrCode(WechatQrcodeApplyLogEntity applyLogEntity) {
        String empWxId = applyLogEntity.getEmpWxId();
        String applyCode = applyLogEntity.getLogicCode();
        //官方的state就用applyCode
        WeChatQrCodeDTO qrCodeDto = wxOfficialConsumerHelper.createQrCode(empWxId, applyCode);

        WechatQrcodeEntity wechatQrcodeEntity = new WechatQrcodeEntity();
        wechatQrcodeEntity.setSource(WechatSourceEnum.OFFICIAL.getCode());
        wechatQrcodeEntity.setApplyCode(applyCode);
        wechatQrcodeEntity.setSourceState(applyCode);
        wechatQrcodeEntity.setQrcodeId(qrCodeDto.getConfigId());
        wechatQrcodeEntity.setQrcodeUrl(qrCodeDto.getQrCode());
        Integer dayInterval = weChatQrCodeConfig.getOfficialQrCodeExpireTimeDayInterval();
        Date expireTime = DateTimeUtil.getTimeOfDayEnd(DateTimeUtil.addDay(new Date(), dayInterval));
        wechatQrcodeEntity.setQrcodeExpireTime(expireTime);
        return wechatQrcodeEntity;
    }

    @Override
    public CustomerWeChatQrCodeDTO genCustomerQrCode(CustomerQrCodeGenReq genReq) {
        //1.根据规则匹配对应的小木管家
        Pair<String, String> empPair = confirmEmpWxId(genReq.getChannelCode(), genReq.getRuleParam());
        if (empPair == null) {
            throw new UgcCrmServiceException("未找符合条件的员工微信id");
        }
        //2.构建申请bo + 发起申请
        WechatQrcodeApplyLogEntity applyLog = applyCustomerQrCode(buildCustomerApplyBo(genReq, empPair));

        //3.生成二维码
        String qrCodeSource = genReq.getQrCodeSource();
        WechatQrcodeEntity qrcodeEntity = genQrCodeWithLock(applyLog, qrCodeSource);
        return buildQrCodDto(applyLog, qrcodeEntity);
    }


    private CustomerWeChatQrCodeDTO buildQrCodDto(WechatQrcodeApplyLogEntity applyLog, WechatQrcodeEntity qrcodeEntity) {
        return CustomerWeChatQrCodeDTO.builder()
                .applyCode(applyLog.getLogicCode())
                .qrCodeSource(qrcodeEntity.getSource())
                .qrCodeUrl(qrcodeEntity.getQrcodeUrl())
                .empWxId(applyLog.getEmpWxId())
                .empName(applyLog.getEmpName())
                .build();
    }


    private CustomerQrCodeApplyBO buildCustomerApplyBo(CustomerQrCodeGenReq req, Pair<String, String> empPair) {
        CustomerQrCodeApplyBO applyBO = new CustomerQrCodeApplyBO();
        //通用字段
        applyBO.setChannelCode(req.getChannelCode());
        applyBO.setUniqueKey(req.getUniqueKey());
        applyBO.setRuleParam(req.getRuleParam());
        //业务相关
        applyBO.setBizType(req.getBizType());
        applyBO.setBizKey(req.getBizKey());
        applyBO.setBizParam(req.getBizParam());
        //客户相关
        applyBO.setCustomerIdType(req.getCustomerIdType());
        applyBO.setCustomerId(req.getCustomerId());
        //员工信息
        applyBO.setEmpWxId(empPair.getLeft());
        applyBO.setEmpName(empPair.getRight());
        return applyBO;
    }

    private Pair<String, String> confirmEmpWxId(String channelCode, Map<String, String> ruleValueMap) {
        //MOCK FIXME 这里记得删掉
        if (CollectionUtils.isEmpty(ruleValueMap)) {
            return Pair.of("hongj", "洪杰");
        }
        List<ChannelCustomerSourceEntity> entityList = channelCustomerSourceLogic
                .queryByChannelInfoIdAndDynamicData(channelCode, ruleValueMap);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        //这里暂时取一个
        ChannelCustomerSourceEntity entity = entityList.get(0);
        return Pair.of(entity.getEmpWxId(), entity.getEmpName());
    }

    @Override
    public void deleteExpireQrCode() {
        Date expireTime = new Date();
        Long lastEndId = 0L;
        Integer size = 500;
        WechatSourceEnum sourceEnum = WechatSourceEnum.OFFICIAL;
        List<WechatQrcodeEntity> waitDelList = iWechatQrcodeService.listExpireQrCode(sourceEnum.getCode(), expireTime, lastEndId, size);
        while (!CollectionUtils.isEmpty(waitDelList)) {
            Long newEndId = waitDelList.get(waitDelList.size() - 1).getId();
            log.info("当前删除过期二维码进度，lastEndId={}，newEndId={}", lastEndId, newEndId);
            deleteQrCode(waitDelList);
            lastEndId = waitDelList.get(waitDelList.size() - 1).getId();
            waitDelList = iWechatQrcodeService.listExpireQrCode(sourceEnum.getCode(), expireTime, lastEndId, size);
        }
    }

    private void deleteQrCode(List<WechatQrcodeEntity> wechatQrCodeList) {
        for (WechatQrcodeEntity qrcodeEntity : wechatQrCodeList) {
            try {
                boolean deleteSuccess = wxOfficialConsumerHelper.deleteQrCode(qrcodeEntity.getQrcodeId());
                if (deleteSuccess) {
                    boolean deleteRet = iWechatQrcodeService.deleteEntityById(qrcodeEntity.getId());
                    log.info("修改删除状态，id={}，deleteRet={}", qrcodeEntity.getId(), deleteRet);
                }
            } catch (Exception e) {
                log.info("调用企微删除二维码异常，");
            }
        }
    }

}
