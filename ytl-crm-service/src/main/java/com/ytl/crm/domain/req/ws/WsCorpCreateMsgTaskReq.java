package com.ytl.crm.domain.req.ws;


import lombok.Data;

import java.util.List;

@Data
public class WsCorpCreateMsgTaskReq {

    /**
     * 发送内容（发送内容与素材id不能同时为空）
     */
    private String text_content;

    /**
     * 群发任务名称,默认任务名称为:[客户触达任务],最大长度为30个字符
     */
    private String task_name;


    /**
     * 【必须】创建者id
     * 如sendself为1，如extra中userid和创建人create_user_id不同，则创建人取值为extra::userid
     */
    private String create_user_id;

    /**
     * 【必须】群发任务类型
     * 10:群发客户-员工一键发送（原企业群发客户）；
     * 11:群发客户群-员工一键发送（原企业群发客户群）；
     * 12:群发朋友圈-员工一键发送（原企业群发朋友圈）；
     * 13:群发客户-通知员工转发（原个人群发客户）；
     * 14:群发客户群-通知员工转发（原个人群发客户群）；
     * 15:群发朋友圈-通知员工转发（原个人群发朋友圈）；
     */
    private Integer type;

    /**
     * 筛选条件扩展类型,1-员工与部门,2-员工与客户 或 员工与客户群.默认值:1
     */
    private Integer range_filter_extra_type;


    /**
     * 【必须】扩展属性
     */
    private String extra;


    /**
     * 是否群发全部客户,默认false.
     * 当type为10,12,13,15时候,该字段生效;
     * 当该字段为true,extra字段的数据失效,当该字段为false,extra字段的数据生效;
     */
    private Boolean is_all_customer;

    /**
     * 是否群发全部客户群,默认false.
     * 当type为11,14时候,该字段生效;
     * 当该字段为true,extra字段的数据失效,当该字段为false,extra字段的数据生效;
     */
    private Boolean is_all_customer_group;

    /**
     * 群发任务结束时间,
     * 格式:时间戳,精度:秒，不填则默认当前时间+48小时。结束时间需大于开始时间最少59分钟，且自动提醒时间不得小于开始时间。
     * 如senself为1，则任务结束时间-任务开始时间不得大于48小时。
     */
    private Long task_end_time;

    /**
     * 素材id集合,素材ID类型为long,集合长度最大为9,发送内容与素材id不能同时为空
     * 注：当material_send_dto有传入素材信息时，material_id_list可以为空
     */
    private List<Integer> material_id_list;


    private List<MaterialSendDto> material_send_dto;

    /**
     * 是否允许成员在待发送客户列表中重新进行选择，默认为false。仅企业群发客户和客户群支持
     */
    private Boolean allow_select;

    /**
     * 发送我的客户，开启则填写1；
     * 仅群发客户-员工一键发送（type=10）和群发朋友圈-员工一键发送（type=12）这两种任务类型填写后生效。
     */
    private int send_self;

    /**
     * 自动提醒时间配置 (1代表15分钟，2代表30分钟，3代表1小时，4代表2小时。默认为2)
     */
    private int time_config;


    @Data
    public static class MaterialSendDto {
        /**
         * 素材id
         */
        private long material_id;

        /**
         * 素材发送方式1.轨迹形式；2.普通形式；（仅文章/网页/文件/视频类型素材支持）
         */
        private int send_type;
    }


    @Data
    public static class Extra<T> {
        private List<T> params;
    }

    /**
     * type=11并且range_filter_extra_type=2 企业群发客户群-员工与客户群
     */
    @Data
    public static class ExtraParam_11_2 {

        private String userid;

        private List<String> chat_id;
    }

    /**
     * type=10
     */
    @Data
    public static class ExtraParam_10 {

        private String userid;

        private List<String> external_userid;
    }

}
