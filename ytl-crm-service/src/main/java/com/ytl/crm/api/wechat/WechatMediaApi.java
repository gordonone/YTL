package com.ytl.crm.api.wechat;

import com.ytl.crm.consumer.wechat.WechatMediaHelper;
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


@RequestMapping("/wechat/file")
@RestController
@Slf4j
public class WechatMediaApi {


    @Autowired
    private WechatMediaHelper wechatMediaHelper;


    /**
     * 上传素材到微信素材库
     *
     * @param file
     */
    @RequestMapping(value = "uploadMediaToWechat", method = RequestMethod.POST)
    @ResponseBody
    public String uploadMediaToWechat(@RequestParam("media") MultipartFile file) throws Exception {

        wechatMediaHelper.getMediaIdFromFile(file, "image");
        return "素材图片上传失败";
    }


    @RequestMapping(value = "getMediaToWechat", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> imageFetch(String mediaId) {

        File file = wechatMediaHelper.imageFetch(mediaId);

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }


}
