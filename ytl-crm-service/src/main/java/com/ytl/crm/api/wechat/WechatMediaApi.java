package com.ytl.crm.api.wechat;

import com.ytl.crm.consumer.wechat.WechatMediaHelper;
import com.ytl.crm.domain.common.BaseResponse;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;


@RequestMapping("/wechat/file")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WechatMediaApi {


    private final WechatMediaHelper wechatMediaHelper;

    /**
     * 上传素材到微信素材库
     *
     * @param file
     */
    @RequestMapping(value = "/uploadMediaToWechat", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> uploadMediaToWechat(@RequestParam("file") MultipartFile file, @RequestParam(name = "type") String fileType) throws Exception {

        String uploadRet = wechatMediaHelper.getMediaIdFromFile(file, fileType);
        return BaseResponse.responseOk(uploadRet);
    }


    @GetMapping(value = "/getMediaToWechat")
    @ResponseBody
    public ResponseEntity<InputStreamResource> imageFetch(@RequestParam(name = "media_id") String mediaId) {

        File file = wechatMediaHelper.imageFetch(mediaId);

        if (Objects.nonNull(file)) {
            try {
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

                return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }


}
