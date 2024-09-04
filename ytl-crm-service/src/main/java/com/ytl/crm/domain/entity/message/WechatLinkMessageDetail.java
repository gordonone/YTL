package com.ytl.crm.domain.entity.message;

import com.ziroom.wechat.service.domain.enumerate.WechatMessageTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/1 15:38
 */
@Getter
@Setter
public class WechatLinkMessageDetail extends BaseWechatMessageDetail {
    public WechatLinkMessageDetail() {
        super(WechatMessageTypeEnum.LINK.getType());
    }

    private String title;

    private String description;

    private String linkUrl;

    private String imageUrl;
}
