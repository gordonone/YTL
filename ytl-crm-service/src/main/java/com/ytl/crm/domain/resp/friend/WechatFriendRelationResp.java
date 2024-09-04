package com.ytl.crm.domain.resp.friend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ytl.crm.domain.constant.Constants;
import lombok.Data;


@Data
public class WechatFriendRelationResp {

    /**
     * 是否添加好友
     */
    private String hasAddFriend;

    /**
     * 是否添加群
     */
    private String hadAddGroup;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 申请码
     */
    private String applyCode;

//    /**
//     * 小程序路由
//     */
//    private CommonRoute xcxRoute;

    @JsonIgnore
    public boolean isAddGroup() {
        return Constants.YES.equals(this.hadAddGroup);
    }

    @JsonIgnore
    public boolean isAddFriend() {
        return Constants.YES.equals(this.hasAddFriend);
    }

    /**
     * 构建加群的响应体
     *
     * @param groupName 群组名称
     * @return 响应体
     */
    public static WechatFriendRelationResp buildHasAddGroupResp(String groupName) {
        WechatFriendRelationResp wechatFriendRelationResp = new WechatFriendRelationResp();
        wechatFriendRelationResp.setHadAddGroup(Constants.YES);
        wechatFriendRelationResp.setHasAddFriend(Constants.YES);
        wechatFriendRelationResp.setGroupName(groupName);
        //  wechatFriendRelationResp.setXcxRoute(new CommonRoute());
        return wechatFriendRelationResp;
    }

    /**
     * 构建加好友的响应体
     *
     * @param empName 管家名称
     * @return 响应体
     */
    public static WechatFriendRelationResp buildHasAddFriendResp(String empName) {
        WechatFriendRelationResp wechatFriendRelationResp = new WechatFriendRelationResp();
        wechatFriendRelationResp.setHadAddGroup(Constants.NO);
        wechatFriendRelationResp.setHasAddFriend(Constants.YES);
        wechatFriendRelationResp.setGroupName(empName);
        return wechatFriendRelationResp;
    }

    /**
     * 构建加好友的响应体
     *
     * @param empName 管家名称
     * @return 响应体
     */
    public static WechatFriendRelationResp buildHasAddFriendResp(String empName, String applyCode) {
        WechatFriendRelationResp wechatFriendRelationResp = new WechatFriendRelationResp();
        wechatFriendRelationResp.setHadAddGroup(Constants.NO);
        wechatFriendRelationResp.setHasAddFriend(Constants.NO);
        wechatFriendRelationResp.setGroupName(empName);
        wechatFriendRelationResp.setApplyCode(applyCode);
        return wechatFriendRelationResp;
    }

}