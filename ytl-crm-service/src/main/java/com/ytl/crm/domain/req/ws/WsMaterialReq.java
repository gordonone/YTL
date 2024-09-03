package com.ytl.crm.domain.req.ws;

import lombok.Data;
@Data
public class WsMaterialReq {

    //素材分类
    private long[] category_id;
    //素材类型 1：海报，2：文本，3：图片，4：文章，6：视频，7：文件，8：小程序，9：网页
    private int type;
    private String keyword;
    //是否是我的素材
    private boolean private_flag;
    //用户id
    private String user_id;
    //create：创建 favorite：收藏 默认/all：全部（当private_flag为true时，需传入参数“create”或者“favorite”）
    private String filter;
    private int current_index;
    private int page_size;
    private long material_id;

    /**
     * 素材使用状态
     * 0使用中 1停用 2未开始 3已过期
     * 默认查询使用中的, 查询全部状态需传空集合
     */
    private int[] use_status;
    //查询的素材ids
    private long[] filter_material_ids;


}
