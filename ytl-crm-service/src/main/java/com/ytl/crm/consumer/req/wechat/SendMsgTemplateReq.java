package com.ytl.crm.consumer.req.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SendMsgTemplateReq {

    @JsonProperty("chat_type")
    public String chat_type;

    //客户的externaluserid列表，仅在chat_type为single时有效，最多可一次指定1万个客户
    @JsonProperty("external_userid")
    public List<String> externalUserid;

    //客户群id列表，仅在chat_type为group时有效，最多可一次指定2000个客户群。
    @JsonProperty("chat_id_list")
    public List<String> chatIdList;

    //否	发送企业群发消息的成员userid，当类型为发送给客户群时必填
    @JsonProperty("sender")
    public String sender;

    @JsonProperty("allow_select")
    public Boolean allowSelect = false;

    public TextContext text;

    public ImageContext image;

    public LinkContext link;

    public videoContext video;

    //文本内容
    @Data
    public static class TextContext {
        public String content;
    }

    //视频内容
    @Data
    public static class videoContext {
        @JsonProperty("media_id")
        public String mediaId;
    }

    //图片内容
    @Data
    public static class ImageContext {

        //    image.media_id	否	图片的media_id，可以通过素材管理接口获得
//    image.pic_url	否	图片的链接，仅可使用上传图片接口得到的链接
        @JsonProperty("media_id")
        public String mediaId;
        @JsonProperty("pic_url")
        public String picUrl;
    }


    @Data
    public static class LinkContext {

//    link.title	是	图文消息标题，最长128个字节
//    link.picurl	否	图文消息封面的url，最长2048个字节
//    link.desc	否	图文消息的描述，最多512个字节
//    link.url	是	图文消息的链接，最长2048个字节

        @JsonProperty("title")
        public String title;

        @JsonProperty("picurl")
        public String picurl;

        @JsonProperty("desc")
        public String desc;

        @JsonProperty("url")
        public String url;


    }


//    tag_filter.group_list.tag_list	否	要进行群发的客户标签列表，同组标签之间按或关系进行筛选，不同组标签按且关系筛选，每组最多指定100个标签，支持规则组标签


//    attachments	否	附件，最多支持添加9个附件
//    attachments.msgtype	是	附件类型，可选image、link、miniprogram、video或者file


//    miniprogram.title	是	小程序消息标题，最多64个字节
//    miniprogram.pic_media_id	是	小程序消息封面的mediaid，封面图建议尺寸为520*416
//    miniprogram.appid	是	小程序appid（可以在微信公众平台上查询），必须是关联到企业的小程序应用
//    miniprogram.page	是	小程序page路径
//    video.media_id	是	视频的media_id，可以通过素材管理接口获得
//    file.media_id	是	文件的media_id，可以通过素材管理接口获得
}
