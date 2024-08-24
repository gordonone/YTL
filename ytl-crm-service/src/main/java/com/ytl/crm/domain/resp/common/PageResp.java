package com.ytl.crm.domain.resp.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageResp<T> implements Serializable {
    private Integer total;
    private List<T> dataList;

    public static <T> PageResp<T> emptyResp() {
        PageResp<T> pageResp = new PageResp<>();
        pageResp.setTotal(0);
        pageResp.setDataList(Collections.emptyList());
        return pageResp;
    }

    public static <T> PageResp<T> buildResp(int total, List<T> data) {
        PageResp<T> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setDataList(data);
        return pageResp;
    }

}
