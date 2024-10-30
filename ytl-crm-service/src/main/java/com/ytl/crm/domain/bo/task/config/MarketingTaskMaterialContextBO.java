package com.ytl.crm.domain.bo.task.config;


import com.ytl.crm.consumer.req.wechat.SendMsgTemplateReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MarketingTaskMaterialContextBO {

    @ApiModelProperty(value = "素材内容")
    private String materialContent;


    public List<SendMsgTemplateReq.MsgContext> attachments;

    @Data
    public static class MsgContext {

        // 可选image、link、miniprogram、video或者file
        //    attachments.msgtype
        private String msgType;

        private MarketingTaskMaterialContextBO.LinkContext link;

        public MarketingTaskMaterialContextBO.ImageContext image;

        public MarketingTaskMaterialContextBO.VideoContext video;

    }

    //视频内容
    @Data
    public static class VideoContext {
        public String mediaId;
    }

    //图片内容
    @Data
    public static class ImageContext {

        //image.media_id	否	图片的media_id，可以通过素材管理接口获得
        //image.pic_url	否	图片的链接，仅可使用上传图片接口得到的链接
        public String mediaId;
        public String picUrl;
    }


    @Data
    public static class LinkContext {

//    link.title	是	图文消息标题，最长128个字节
//    link.picurl	否	图文消息封面的url，最长2048个字节
//    link.desc	否	图文消息的描述，最多512个字节
//    link.url	是	图文消息的链接，最长2048个字节

        public String title;

        public String picurl;

        public String desc;

        public String url;


    }


}
