package com.ytl.crm.config;

import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class WeChatThirdSourceConfig {

   // @ApolloJsonValue("${weChat.thirdSource.baseInfoMap:{}}")
    private Map<String, ThirdSourceBaseInfo> thirdSourceBaseInfoMap;

    @Data
    @AllArgsConstructor
    public static class ThirdSourceBaseInfo {
        private String agentId;
    }

    public ThirdSourceBaseInfo acquireThirdSourceBaseInfo(WechatSourceEnum sourceEnum) {
        return thirdSourceBaseInfoMap.get(sourceEnum.getCode());
    }

    public String acquireThirdSourceAgentId(WechatSourceEnum sourceEnum) {
        ThirdSourceBaseInfo baseInfo = acquireThirdSourceBaseInfo(sourceEnum);
        return baseInfo != null ? baseInfo.getAgentId() : StringUtils.EMPTY;
    }

}