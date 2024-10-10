package com.ytl.crm.domain.bo.wechat;

import lombok.Data;

import java.util.Date;

@Data
public class WechatFriendSaveBO {

    /**
     * 客户官方微信id
     */
    private String customerWxId;

    /**
     * 员工官方微信id
     */
    private String empWxId;

    /**
     * 申请记录
     */
    private String applyCode;

    /**
     * 添加时间
     */
    private Date addTime;


}
