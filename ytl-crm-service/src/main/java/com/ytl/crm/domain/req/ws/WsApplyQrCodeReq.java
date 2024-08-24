package com.ytl.crm.domain.req.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 14:56
 * 申请活码入参
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WsApplyQrCodeReq {
    /**
     * 创建人 必填
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * 活码名称 必填
     */
    @JsonProperty("qrcode_name")
    private String qrcodeName;

    /**
     * 活码类型：1:单人，2：多人 必填
     */
    @JsonProperty("type")
    private Integer type;

    /**
     * 用户列表
     */
    @JsonProperty("user_list")
    private List<User> userList;

    /**
     * 验证标志，1-自动通过，2-手动通过，3-分时段自动通过 必填 默认1
     */
    @JsonProperty("without_confirm")
    private Integer withoutConfirm;

    /**
     * 欢迎语列表
     */
    @JsonProperty("greeting_list")
    private List<Greeting> greetingList;

    /**
     * 活码头像logo，可入参图片url
     */
    @JsonProperty("logo_img")
    private String logoImg;

    /**
     * 使用员工是否可推广活码，0 否，1 是，默认是
     * <a href="https://worktop.ziroom.com/#/tech-demand-detail?demandId=1823189718247424001&uniqueSign=R-18416">传入否，不提醒</a>
     */
    @JsonProperty("staff_promotion_flag")
    private Integer staffPromotionFlag;

    /**
     * 自定义备注名
     */
    @JsonProperty("custom_remark")
    private String customRemark;

    /**
     * 用户列表
     */
    @Data
    public static class User {
        @JsonProperty("user_id")
        private String userId;
    }

    /**
     * 欢迎语列表
     */
    @Data
    public static class Greeting {
        @JsonProperty("greeting")
        private String greeting;
    }
}
