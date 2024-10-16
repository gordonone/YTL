package com.ytl.crm.constants;

import lombok.experimental.UtilityClass;


@UtilityClass
public class WxOfficialCallBackConstant {

    public static class ParamConstant {

        /**
         * 企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体
         */
        public static final String MSG_SIGNATURE = "msg_signature";

        /**
         * 时间戳。与nonce结合使用，用于防止请求重放攻击。
         */
        public static final String TIMESTAMP = "timestamp";

        /**
         * 随机数。与timestamp结合使用，用于防止请求重放攻击。
         */
        public static final String NONCE = "nonce";

        /**
         * 加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文
         */
        public static final String ECHO_STR = "echostr";

        /**
         * 消息结构体加密后的字符串
         */
        public static final String FIELD_ENCRYPT = "Encrypt";

        /**
         * 接收的应用id，可在应用的设置页面获取。仅应用相关的回调会带该字段。
         */
        public static final String FIELD_AGENT_ID = "AgentID";

        /**
         * 回调请求体 企业微信的CorpID，当为第三方应用回调事件时，CorpID的内容为suiteid
         */
        public static final String FIELD_TO_USER_NAME = "ToUserName";

    }


    public static class DataConstant {

        /**
         * 接收的应用id，可在应用的设置页面获取。仅应用相关的回调会带该字段。
         */
        public static final String FIELD_AGENT_ID = "agentID";

        /**
         * 回调请求体 企业微信的CorpID，当为第三方应用回调事件时，CorpID的内容为suiteid
         */
        public static final String FIELD_TO_USER_NAME = "toUserName";


    }

}       
