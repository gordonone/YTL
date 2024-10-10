package com.ytl.crm.mq.model.wechat.official;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxOfficialCallbackMsgDTO implements Serializable {

    /**
     * event 事件
     */
    public static final String FILED_EVENT = "event";

    /**
     * changeType 时间详情
     */
    public static final String FILED_CHANGE_TYPE = "changeType";

    /**
     * 事件id - 用于查问题
     */
    private String traceId;

    /**
     * 时间戳 - 秒
     */
    private String timestamp;

    /**
     * 随机数
     */
    private String nonce;

    /**
     * 参数
     */
    private JSONObject data;

}
