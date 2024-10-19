package com.ytl.crm.consumer.dto.req.ws;

import lombok.Data;

import java.util.List;

@Data
public class WsMaterialReq {

    //素材分类
    private Long[] category_id;
    //素材类型 1：海报，2：文本，3：图片，4：文章，6：视频，7：文件，8：小程序，9：网页
    private Integer type;
    private String keyword;
    //是否是我的素材
    private Boolean private_flag;
    //用户id
    private String user_id;
    //create：创建 favorite：收藏 默认/all：全部（当private_flag为true时，需传入参数“create”或者“favorite”）
    private String filter;
    private Integer current_index;
    private Integer page_size;
    private Long material_id;

    /**
     * 素材使用状态
     * 0使用中 1停用 2未开始 3已过期
     * 默认查询使用中的, 查询全部状态需传空集合
     */
    private List<Integer> use_status;
    //查询的素材ids
    private Long[] filter_material_ids;


}
