package com.ytl.crm.logic.dict.interfaces;



import com.ytl.crm.domain.req.dict.SysDictQueryByTypeReq;
import com.ytl.crm.domain.resp.dict.SysDictDTO;

import java.util.List;

public interface ISysDictLogic {
    List<SysDictDTO> listByDictType(SysDictQueryByTypeReq req);
}
