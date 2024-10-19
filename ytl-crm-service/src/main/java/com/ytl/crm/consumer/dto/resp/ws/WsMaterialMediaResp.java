package com.ytl.crm.consumer.dto.resp.ws;

import lombok.Data;

import java.util.List;

@Data
public class WsMaterialMediaResp {


    private Integer total;

    private List<Media> records;

    @Data
    public static class Media {

        private long id;

        private long category_id;

        private long category_pid;

        private int type;

        //0:长期有效 2:自定义有效时间 3:停用（默认长期有效）
        private int publish_status;

        private String creator;

        private long create_time;

        //有效开始时间
        private long start_time;

        //有效结束时间
        private long end_time;

        //海报部分（标题）
        private String title;

        //海报快照url
        private String snapshot_url;

        //海报缩略图url
        private String thumbnail_url;

        //初始下载量
        private int initial_download;

        //海报类型
        private int poster_type;

        //文本素材
        //文本内容
        private String plain_text;

        //图片素材
        private String img_url;

        //图片文件id
        private long file_id;

        //文章、视频素材：
        //摘要信息
        private String abstract_info;

        //封面图文件id
        private String image_base;

        //封面图文件url
        private String image_base_url;

        //封面图文件url
        //文章类型（1：自定义文章）
        private Integer web_type;

        //文章地址
        private String web_url;

        //外部链接
        //连接地址
        private String origin_link;
        //短连接地址
        private String short_link;
    }

}
