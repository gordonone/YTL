package com.ytl.crm.api.dict;


import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.req.dict.SysDictQueryByTypeReq;
import com.ytl.crm.domain.resp.dict.SysDictDTO;
import com.ytl.crm.logic.dict.interfaces.ISysDictLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(value = "字典相关", tags = "字典相关API")
@RestController
@RequestMapping("/common/dict")
@RequiredArgsConstructor
public class SysDictApi {

    private final ISysDictLogic iSysDictLogic;

    @PostMapping("/queryByType")
    @ApiOperation(value = "根据类型查询")
    public BaseResponse<List<SysDictDTO>> queryByType(@RequestBody @Valid SysDictQueryByTypeReq req) {
        return BaseResponse.responseOk(iSysDictLogic.listByDictType(req));
    }
}
