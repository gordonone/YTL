package com.ytl.crm.domain.enums;

public enum BaseResponseCodeEnum {
    UNDEFINED(-1, "未定义"),
    OK(200, "操作成功"),
    FAIL(999, "操作失败"),
    SYSTEM_ERROR(999999, "系统异常");

    private Integer code;
    private String msg;

    private BaseResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BaseResponseCodeEnum parse(Integer code) {
        if (code == null) {
            return UNDEFINED;
        } else {
            BaseResponseCodeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                BaseResponseCodeEnum rentFileTypeEnum = var1[var3];
                if (rentFileTypeEnum.getCode().equals(code)) {
                    return rentFileTypeEnum;
                }
            }

            return UNDEFINED;
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
