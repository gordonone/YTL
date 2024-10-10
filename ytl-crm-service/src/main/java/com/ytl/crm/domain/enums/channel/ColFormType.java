package com.ytl.crm.domain.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ColFormType {
    //1 文本编辑  2 下拉列表单选 3 下拉列表多选 4 radio 5 checkbox
    TEXT_EDIT(1, "文本编辑"),
    SELECT_SINGLE(2, "下拉列表单选"),
    SELECT_MULTI(3, "下拉列表多选"),
    RADIO(4, "radio"),
    CHECKBOX(5, "checkbox");

    private Integer code;
    private String desc;

}
