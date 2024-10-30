package com.ytl.crm.api.task.config;


import com.ytl.crm.domain.bo.task.config.MarketingTaskConfigAddBO;
import com.ytl.crm.domain.bo.task.config.MarketingTaskMaterialContextBO;
import com.ytl.crm.domain.common.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Api(value = "任务素材配置API", tags = "任务素材配置API")
@RestController
@RequestMapping("/task/material")
@RequiredArgsConstructor
public class MarketingTaskMaterialContentApi {



    @PostMapping("/add")
    @ApiOperation(value = "任务配置-新增素材内容配置")
    public BaseResponse<Boolean> saveTaskMaterialContent(@RequestBody @Valid MarketingTaskMaterialContextBO marketingTaskMaterialContextBO) {

        return BaseResponse.responseOk(true);

    }

}
