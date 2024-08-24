package com.ytl.crm.constants;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class CommonConstant {

    public static final String ZIRUKE = "自如客";
    public static final String ZIROOM = "ziroom";
    public static final String SYSTEM = "system";
    public static final String SYSTEM_NAME = "系统";


    /**
     * 英文逗号
     */
    public static final String COMMA = ",";
    public static final Splitter COMMA_SPLITTER = Splitter.on(COMMA);
    public static final Joiner COMMA_JOINER = Joiner.on(COMMA);

    /**
     * 中文逗号
     */
    public static final String CH_COMMA = "，";
    public static final Splitter CH_COMMA_SPLITTER = Splitter.on(CH_COMMA);
    public static final Joiner CH_COMMA_JOINER = Joiner.on(CH_COMMA);

    /**
     * 英文分号
     */
    public static final String SEMICOLON = ";";
    public static final Splitter SEMICOLON_SPLITTER = Splitter.on(SEMICOLON);

    /**
     * 下划线
     */
    public static final String UNDER_LINE = "_";
    public static final Splitter UNDER_LINE_SPLITTER = Splitter.on(UNDER_LINE);

    /**
     * 英文冒号
     */
    public static final String COLON = ":";

    /**
     * 顿号
     */
    public static final String DUN_HAO = "、";
    public static final Splitter DUN_HAO_SPLITTER = Splitter.on(DUN_HAO);
    public static final Joiner DUN_HAO_JOINER = Joiner.on(DUN_HAO);

    /**
     * 横线
     */
    public static final char LINE = '-';
    public static final Splitter LINE_SPLITTER = Splitter.on(LINE);
    public static final Joiner LINE_JOINER = Joiner.on(LINE);

    /**
     * 星号
     */
    public static final String START = "*";

}
