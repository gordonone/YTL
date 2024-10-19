package com.ytl.crm.consumer.dto.resp.storage;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上传记录唯一id
     */
    private String id;

    /**
     * 上传记录文件key
     */
    private String key;

    /**
     * 上传记录文件名称
     */
    private String originalFilename;

    /**
     * 文件访问url
     */
    private String url;

    /**
     * 上传时间
     */
    private String uploadTime;

    /**
     * 是否涉黄
     */
    private String isPorn;

    /**
     * 识别结果
     */
    private String hasFace;

}
