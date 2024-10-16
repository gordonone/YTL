package com.ytl.crm.logic.wechat.interfaces;



import com.ytl.crm.domain.bo.wechat.WechatFriendSaveBO;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.domain.resp.wechat.WechatFriendDetailDTO;

import java.util.List;

public interface IWechatFriendLogic {

    /**
     * 通过自如uid进行查询
     */
    List<WechatFriendDetailDTO> queryFriendByCustomerId(String customerIdType, String customerId);

    /**
     * 保存好友关系
     */
    boolean saveFriend(WechatFriendSaveBO saveBO);

    /**
     * 删除好友
     */
    void delFriend(String empWxId, String customerWxId);

    /**
     * 更新好友信息
     */
    void updateFriend(WechatFriendRelationEntity oldFriendRelation);

}
