package com.ytl.crm.service.ws.utils;

import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsEmpDetailResp;
import com.ziroom.ugc.footstone.commons.domain.BaseResponse;
import com.ziroom.wechat.service.domain.resp.crm.CrmWcbBaseResp;
import com.ziroom.wechat.service.domain.resp.ws.WsBaseResponse;

import java.util.Objects;
import java.util.Optional;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 11:28
 */
public class ResponseUtils {

    private ResponseUtils() {
    }

    /**
     * 解析响应结果
     *
     * @param response 响应结果
     * @param message  提示信息
     * @param <T>      泛型
     * @return 响应结果体
     */
    public static <T> T parseBaseResponse(BaseResponse<T> response, String message) {
        PreconditionsUtils.checkBusiness(Objects.nonNull(response), message);
        PreconditionsUtils.checkBusiness(response.isOK(), Optional.ofNullable(response.getMsg()).orElse(message));
        return response.getData();
    }

    /**
     * 解析响应结果
     *
     * @param response 响应结果
     * @param message  提示信息
     * @param <T>      泛型
     * @return 响应结果体
     */
    public static <T> T parseWsBaseResponse(WsBaseResponse<WsEmpDetailResp> response, String message) {
        PreconditionsUtils.checkBusiness(Objects.nonNull(response), message);
        PreconditionsUtils.checkBusiness(response.isOk(), Optional.ofNullable(response.getMsg()).orElse(message));
        return response.getData();
    }

    /**
     * 解析响应结果
     *
     * @param response 响应结果
     * @param message  提示信息
     * @param <T>      泛型
     * @return 响应结果体
     */
    public static <T> T parseWsBaseResponseWithData(WsBaseResponse<T> response, String message) {
        PreconditionsUtils.checkBusiness(Objects.nonNull(response), message);
        PreconditionsUtils.checkBusiness(response.isOk(), Optional.ofNullable(response.getMsg()).orElse(message));
        PreconditionsUtils.checkBusiness(Objects.nonNull(response.getData()), message);
        return response.getData();
    }

    /**
     * 解析响应结果
     *
     * @param response 响应结果
     * @param message  提示信息
     * @param <T>      泛型
     * @return 响应结果体
     */
    public static <T> T parseCrmWcbBaseResponse(CrmWcbBaseResp<T> response, String message) {
        PreconditionsUtils.checkBusiness(Objects.nonNull(response), message);
        PreconditionsUtils.checkBusiness(response.success(), Optional.ofNullable(response.getError_message()).orElse(message));
        return response.getData();
    }
}
