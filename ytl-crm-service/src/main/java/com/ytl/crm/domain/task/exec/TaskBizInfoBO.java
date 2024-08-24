package com.ytl.crm.domain.task.exec;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class TaskBizInfoBO {

    /**
     * 客户uid
     */
    private String uid;

    /**
     * 合同号
     */
    private String contractCode;

    /**
     * 群号 - logicCode
     */
    private String groupCode;

    /**
     * 用户三方id-微盛
     */
    private String customerThirdId;

    /**
     * 用户姓名 - 为空
     */
    private String customerName;

    /**
     * 群三方code-微盛
     */
    private String groupThirdCode;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 小木管家虚拟号
     */
    private String virtualKeeperId;

    /**
     * 小木管家虚拟号-三方id-微盛
     */
    private String virtualKeeperThirdId;

    /**
     * 小木管家虚拟号-姓名
     */
    private String virtualKeeperName;

    /**
     * 是否有效
     */
    private Boolean isValid;

    /**
     * 异常数据
     */
    private String notValidMsg;

    private static final String FIELD_MISS_MSG = "字段信息缺失";

    public static TaskBizInfoBO notValidInfo(String uid, String notValidMsg) {
        TaskBizInfoBO bizInfoBO = new TaskBizInfoBO();
        bizInfoBO.setUid(uid);
        bizInfoBO.setIsValid(false);
        bizInfoBO.setNotValidMsg(notValidMsg);
        return bizInfoBO;
    }

    public boolean checkValid() {
        boolean checkRet = StringUtils.isNoneBlank(this.uid, this.groupCode, this.groupThirdCode,
                this.virtualKeeperId, this.virtualKeeperThirdId);
        this.isValid = checkRet;
        if (!checkRet) {
            notValidMsg = FIELD_MISS_MSG;
        }
        return checkRet;
    }

}
