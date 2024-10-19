package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 成员对外信息
 * https://work.weixin.qq.com/api/doc/90000/90135/92230
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalUserProfileDTO {

    @JsonProperty("external_attr")
    private List<ExtAttr> externalAttr;

    /**
     * 属性列表，目前支持文本、网页、小程序三种类型
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExtAttr {

        /**
         * 属性类型: 0-文本 1-网页 2-小程序
         */
        private String type;

        /**
         * 属性名称： 需要先确保在管理端有创建该属性，否则会忽略
         */
        private String name;

        /**
         * 文本类型属性
         */
        private ExternalAttrText text;

        /**
         * web 类型属性
         */
        private ExternalAttrWeb web;

        /**
         * 小程序类型属性
         */
        @JsonProperty("miniprogram")
        private ExternalAttrMiniprogram miniprogram;
    }

    /**
     * 文本类型的属性
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExternalAttrText {

        /**
         * 文本属性内容,长度限制12个UTF8字符
         */
        private String value;
    }

    /**
     * 网页类型的属性，url和title字段要么同时为空表示清除该属性，要么同时不为空
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExternalAttrWeb {

        /**
         * 网页的url,必须包含http或者https头
         */
        private String url;

        /**
         * 网页的展示标题,长度限制12个UTF8字符
         */
        private String title;

    }

    /**
     * 小程序类型的属性，appid和title字段要么同时为空表示清除改属性，要么同时不为空
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExternalAttrMiniprogram {

        /**
         * 小程序appid，必须是有在本企业安装授权的小程序，否则会被忽略
         */
        private String appid;

        /**
         * 小程序的展示标题,长度限制12个UTF8字符
         */
        private String title;

        /**
         * 小程序的页面路径
         */
        private String pagepath;
    }

}       
