//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ytl.crm.consumer.dto.req.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileUploadRequest extends BaseReq {
    private String filename;
    private Object object;
    private Long size;
    private String type;
    private Long activeTime;
    private String watermark;
    private Boolean thumbnailFlag;
    private String source;
    private String userCode;
    private Boolean useFileName;
    private Boolean contentSecurity;
    private Boolean porn;
    @JsonProperty("limitRatio")
    private Double limitRatio;
    private Boolean face;

}
