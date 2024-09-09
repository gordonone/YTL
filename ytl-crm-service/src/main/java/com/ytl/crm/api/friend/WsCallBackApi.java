package com.ytl.crm.api.friend;

import com.alibaba.fastjson.JSONPath;
import com.ytl.crm.domain.enums.friend.WsCallBackTypeEnum;
import com.ytl.crm.service.ws.Impl.WechatFriendLogicService;
import com.ytl.crm.service.ws.Impl.WechatGroupLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/19 9:29
 * 微盛企微回调
 */
@Slf4j
@RestController
@RequestMapping("/ws/callback")
public class WsCallBackApi {

    @Resource
    private WechatFriendLogicService wechatFriendLogicService;

    @Resource
    private WechatGroupLogicService wechatGroupLogicService;

    /**
     * 好友关系事件回调
     */
    @PostMapping("/notify")
    public String notify(@RequestBody String json) {
        try {
            String type = String.valueOf(JSONPath.eval(json, "$.type"));
            String content = String.valueOf(JSONPath.eval(json, "$.content"));
            String messageId = String.valueOf(JSONPath.eval(json, "$.message_id"));
            ;
            WsCallBackTypeEnum wsCallBackTypeEnum = WsCallBackTypeEnum.parse(type);
            switch (wsCallBackTypeEnum) {
                case FRIEND_CHANGE:
                    log.info("微盛企微回调入参(好友)：{}", json);
                    wechatFriendLogicService.processFriendChangeEvent(messageId, content);
                    break;
                case GROUP_CHANGE:
                    break;
                default:
                    log.info("微盛企微回调未监听类型 type:{} json：{}", type, json);
                    break;
            }
        } catch (Exception e) {
            log.error("回调事件处理异常，异常原因", e);
            return "FAIL";
        } finally {

        }
        return "SUCCESS";
    }

    public static void main(String[] args) {
        String json = "{\n" + "  \"message_id\": \"1529000206320803842\",\n" + "  \"type\": 16,\n" + "  \"content\": \"{\\\"tenant_id\\\":\\\"test_c5feb56fb838\\\",\\\"chat_id\\\":\\\"test_63de341f7ae7\\\",\\\"change_type\\\":18,\\\"user_id\\\":\\\"test_d099673840d7\\\",\\\"user_type\\\":85,\\\"join_scene\\\":3,\\\"join_time\\\":70,\\\"invitor\\\":\\\"test_3483c6794734\\\",\\\"group_nick_name\\\":\\\"test_9330d0b6ee0c\\\",\\\"name\\\":\\\"test_88a73214a15a\\\"}\",\n" + "  \"gmt_create\": 1653377031\n" + "}";


        //   System.out.println("content：" + content);
        System.out.println(System.currentTimeMillis());
    }


}
