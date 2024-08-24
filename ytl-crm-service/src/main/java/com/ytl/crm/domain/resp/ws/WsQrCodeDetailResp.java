package com.ytl.crm.domain.resp.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 14:45
 */
@NoArgsConstructor
@Data
public class WsQrCodeDetailResp {

    /**
     * 活码标识
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 活码类型：1:单人，2：多人
     */
    @JsonProperty("type")
    private Integer type;

    /**
     * 员工活码名称
     */
    @JsonProperty("qrcode_name")
    private String qrcodeName;

    /**
     * 引导语
     */
    @JsonProperty("guide_msg")
    private String guideMsg;

    /**
     * 新增联系方式的配置id
     */
    @JsonProperty("config_id")
    private String configId;

    /**
     * 企业自定义的state参数，用于区分不同的添加渠道，不超过30个字符
     */
    @JsonProperty("state")
    private String state;

    /**
     * 验证标志，1自动通过，2手动通过，3_分时段自动通过
     */
    @JsonProperty("without_confirm")
    private Integer withoutConfirm;

    /**
     * 添加方式，1-全天可添加，2-分时段添加
     */
    @JsonProperty("multiple_type")
    private Integer multipleType;

    /**
     * 联系二维码地址
     */
    @JsonProperty("qr_code")
    private String qrCode;

    /**
     * 二维码图片地址
     */
    @JsonProperty("qr_code_img_url")
    private String qrCodeImgUrl;

    /**
     * 每日添加上限标志：true:开启，false:关闭
     */
    @JsonProperty("day_add_up_mark")
    private Boolean dayAddUpMark;

    /**
     * 活码下的员工id
     */
    @JsonProperty("staff_user_ids")
    private List<String> staffUserIds;

    /**
     * 员工具体信息
     */
    @JsonProperty("user_list")
    private List<User> userList;

    /**
     * 活码分组id
     */
    @JsonProperty("qrcode_group_id")
    private Long qrcodeGroupId;

    /**
     * 自定义备注名
     */
    @JsonProperty("custom_remark")
    private String customRemark;

    /**
     * 活码模板
     */
    @JsonProperty("qrcode_model")
    private String qrcodeModel;

    /**
     * 活码模板url
     */
    @JsonProperty("qrcode_model_url")
    private String qrcodeModelUrl;

    /**
     * 模板标题
     */
    @JsonProperty("model_title")
    private String modelTitle;

    /**
     * 使用员工是否可推广活码，0 否，1 是，默认是
     */
    @JsonProperty("staff_promotion_flag")
    private Integer staffPromotionFlag;

    /**
     * 获客渠道
     */
    @JsonProperty("channel_ids")
    private String channelIds;

    /**
     * 活码短链地址
     */
    @JsonProperty("short_link")
    private String shortLink;

    /**
     * 创建人user_id
     */
    @JsonProperty("create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private String createTime;

    /**
     * 欢迎语类型
     */
    @JsonProperty("greeting_type")
    private Integer greetingType;

    /**
     * 欢迎语列表
     */
    @JsonProperty("greeting_list")
    private List<Greeting> greetingList;


    @Data
    public static class User {
        /**
         * 部门id
         */
        @JsonProperty("dept_id")
        private String deptId;
        /**
         * 员工id
         */
        @JsonProperty("user_id")
        private String userId;
        /**
         * 每日添加上限
         */
        @JsonProperty("day_add_up_number")
        private Integer dayAddUpNumber;
        /**
         * 员工人数添加上限数
         */
        @JsonProperty("staff_count_up_number")
        private Integer staffCountUpNumber;
        /**
         * 是否备用员工
         */
        @JsonProperty("is_standby_staff")
        private Boolean isStandbyStaff;
    }

    @Data
    public static class Greeting {
        /**
         * 欢迎语id
         */
        @JsonProperty("id")
        private Long id;
        /**
         * 欢迎语类型，1：固定欢迎语，2：分时段欢迎语
         */
        @JsonProperty("greeting_type")
        private Integer greetingType;
        /**
         * 欢迎语模板id
         */
        @JsonProperty("greeting_model")
        private String greetingModel;

        /**
         * 欢迎语内容
         */
        @JsonProperty("greeting")
        private String greeting;

        /**
         * 星期，1:周一，2：周二，3：周三，4：周四，5：周五，6：周六，7：周日
         */
        @JsonProperty("week_day")
        private List<String> weekDay;
        @JsonProperty("start_time")
        private String startTime;
        @JsonProperty("end_time")
        private String endTime;
        @JsonProperty("delete_status")
        private Boolean deleteStatus;
        @JsonProperty("material_info_list")
        private List<MaterialInfoList> materialInfoList;

        @Data
        public static class MaterialInfoList {
            @JsonProperty("id")
            private Long id;
            @JsonProperty("type")
            private Integer type;
            @JsonProperty("combination_id")
            private Long combinationId;
            @JsonProperty("package_config_id")
            private Long packageConfigId;
            @JsonProperty("send_type")
            private Integer sendType;
        }
    }
}
