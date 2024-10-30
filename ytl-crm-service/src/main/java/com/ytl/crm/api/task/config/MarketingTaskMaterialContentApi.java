package com.ytl.crm.api.task.config;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "任务素材配置API", tags = "任务素材配置API")
@RestController
@RequestMapping("/task/material")
@RequiredArgsConstructor
public class MarketingTaskMaterialContentApi {
}
