package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionThirdTaskExecStatus {
    UNKNOWN("UNKNOWN", "未知", -999),
    CREATE_FAIL("CREATE_FAIL", "创建任务失败", -1),
    NOT_EXEC("NOT_EXEC", "未执行", 0),
    EXEC_SUCCESS("EXEC_SUCCESS", "执行成功", 1),
    EXEC_FAIL("EXEC_FAIL", "执行失败", 2),
    ;
    private final String code;
    private final String desc;

    /**
     * 微盛-任务执行状态，-1.创建任务失败；0.未执行；1.执行成功；2.执行失败
     */
    private final Integer wsStatus;

    public static ActionThirdTaskExecStatus queryByWsStatus(Integer status) {
        for (ActionThirdTaskExecStatus statusEnum : ActionThirdTaskExecStatus.values()) {
            if (statusEnum.wsStatus.equals(status)) {
                return statusEnum;
            }
        }
        return UNKNOWN;
    }

}
