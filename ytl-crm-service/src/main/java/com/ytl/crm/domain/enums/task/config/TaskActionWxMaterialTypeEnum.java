package com.ytl.crm.domain.enums.task.config;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionWxMaterialTypeEnum implements EnumWithCodeAndDesc<String> {

    image("image", "图片"),
    voice("voice", "语音"),
    video("video", "视频"),
    file("file", "普通文件");


    //分别有图片（image）、语音（voice）、视频（video），普通文件(file)

    private final String code;
    private final String desc;
}
