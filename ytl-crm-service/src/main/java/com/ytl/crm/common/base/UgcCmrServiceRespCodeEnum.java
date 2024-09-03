package com.ytl.crm.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 *
 * @author hongj
 * @date 16/9/2023 下午5:29
 */
@Getter
@AllArgsConstructor
public enum UgcCmrServiceRespCodeEnum {

    UNDEFINED(-1, "未定义"),
    OK(200, "操作成功"),
    FAIL(999, "操作失败"),
    SYSTEM_ERROR(100001, "系统繁忙,请稍后再试"),
    PARAM_CHECK_ERROR(100002, "参数校验异常"),
    VALIDATE_ERROR(100003, "参数校验异常"),
    DUPLICATE_KEY_ERROR(100004, "数据重复，请检查后提交"),
    NULL_POINTER_EXCEPTION_ERROR(100005, "系统繁忙,请稍后再试"),
    HAS_NO_DATA_ERROR(100006, "无实际数据，请检查查询条件"),
    INVOKE_URL_ERROR(100007, "接口请求URL配置异常，未获取到相关配置信息"),
    INVOKE_IO_ERROR(100008, "接口请求异常,请稍后重试"),
    ;

    private Integer code;
    private String msg;

}
