package com.ytl.crm.domain.entity.message;

import com.ziroom.wechat.service.domain.enumerate.WechatMessageTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/8 10:21
 */
@Getter
@Setter
public class WechatOtherMessageDetail extends BaseWechatMessageDetail {
    public WechatOtherMessageDetail() {
        super(WechatMessageTypeEnum.OTHER.getType());
        this.content = "暂不支持的消息类型";
    }

    /**
     * 默认内容
     */
    private String content;
}
