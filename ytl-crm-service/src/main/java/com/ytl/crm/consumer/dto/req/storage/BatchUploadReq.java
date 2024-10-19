package com.ytl.crm.consumer.dto.req.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BatchUploadReq extends BaseReq {
    private List<MultipartFile> files;
}
