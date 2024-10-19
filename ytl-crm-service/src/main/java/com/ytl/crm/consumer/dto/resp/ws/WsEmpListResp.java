package com.ytl.crm.consumer.dto.resp.ws;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: ugc-wechat-service
 * <p></p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * @description:
 * @author: lijh99
 * @create: 2024-07-19 18:22
 * @version: V1.0
 **/
@Data
@NoArgsConstructor
public class WsEmpListResp {

    private int total;

    private List<WsUserDetail> records;

    @NoArgsConstructor
    @Data
    public static class WsUserDetail {


        /**
         * 员工名称，仅安装代开发自建应用且授权了员工名称权限后可获取。
         */
        private String username;

        /**
         * 员工id，员工在本三方应用的密文用户id
         */
        private String userId;

        /**
         * 员工别名，仅安装代开发自建应用且授权了员工名称权限后可获取。
         */
        private String alias;

        /**
         * 手机号码；从2022年6月20号20点开始，新创建/移入应用可见范围的员工不再返回此信息。
         */
        private String mobile;

        /**
         * 职务
         */
        private String position;

        /**
         * 联系邮箱；从2022年6月20号20点开始，新创建/移入应用可见范围的员工不再返回此信息。
         */
        private String email;

        /**
         * 头像地址
         */
        private String avatar;
        /**
         * 主部门id
         */
        private String main_department_id;
        /**
         * 成员所属部门id列表
         */
        private String department_id;

        /**
         * 成员激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。已激活代表已激活企业微信或已关注微信插件（原企业号）。未激活代表既未激活企业微信又未关注微信插件（原企业号）。
         */
        private String status;

        /**
         * 成员开通本应用使用权限状态：1未开通 2绑定并开通 3已过期
         */
        private String account_status;
        /**
         * 成员在企业微信会话存档的帐号开通状态： 0开通 1未开通 2历史开通
         */
        private String chat_data_status;
        /**
         * 员工二维码
         */
        private String qr_code;
    }
}
