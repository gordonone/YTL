package com.ytl.crm.api.task.config;



import com.ytl.crm.domain.bo.task.config.MarketingTaskConfigAddBO;
import com.ytl.crm.domain.bo.task.config.MarketingTaskMediaBO;
import com.ytl.crm.domain.bo.task.config.MarketingTaskQueryBO;
import com.ytl.crm.domain.bo.task.config.MarketingTaskStatusBO;
import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.config.resp.TaskConfigConstantResp;
import com.ytl.crm.domain.resp.ws.WsMaterialMediaResp;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskConfigBO;
import com.ytl.crm.service.ws.define.task.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@Api(value = "任务配置API", tags = "任务配置API")
@RestController
@RequestMapping("/task/config")
@RequiredArgsConstructor
public class MarketingTaskConfigApi {


    private final IMarketingTaskConfigLogic iMarketingTaskConfigLogic;
    private final IMarketingTaskService imarketingTaskService;


    @PostMapping("/page")
    @ApiOperation(value = "任务配置-列表")
    public BaseResponse<PageResp<MarketingTaskEntity>> queryTaskList(@RequestBody @Valid MarketingTaskQueryBO marketingTaskQueryBO) {
        return BaseResponse.responseOk(imarketingTaskService.queryTaskList(marketingTaskQueryBO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "任务配置-新增配置")
    public BaseResponse<Boolean> saveTaskConfig(@RequestBody @Valid MarketingTaskConfigAddBO marketingTaskConfigBO) {
        return BaseResponse.responseOk(iMarketingTaskConfigLogic.saveTaskConfig(marketingTaskConfigBO));
    }

    @PostMapping("/updateTaskConfigStatus")
    @ApiOperation(value = "任务配置-更新任务状态")
    public BaseResponse<Boolean> updateTaskConfigStatus(@RequestBody @Valid MarketingTaskStatusBO marketingTaskStatusBO) {
        return BaseResponse.responseOk(iMarketingTaskConfigLogic.updateTaskConfig(marketingTaskStatusBO));
    }


    @GetMapping("/detail")
    @ApiOperation(value = "任务配置-任务详情")
    public BaseResponse<MarketingTaskConfigBO> queryTaskDetail(@RequestParam(name = "taskCode") @Valid String taskCode) {
        return BaseResponse.responseOk(iMarketingTaskConfigLogic.queryTaskDetail(taskCode));
    }


    @PostMapping("/constant")
    @ApiOperation(value = "任务配置-任务常量")
    public BaseResponse<TaskConfigConstantResp> queryTaskConstant() {
        return BaseResponse.responseOk(iMarketingTaskConfigLogic.queryTaskConstant());
    }

    @PostMapping("/previewWsMaterialMedia")
    @ApiOperation(value = "任务配置-校验素材是否正确")
    public BaseResponse<WsMaterialMediaResp.Media> previewWsMaterialMedia(@RequestBody @Valid MarketingTaskMediaBO marketingTaskMediaBO) {
        WsMaterialMediaResp.Media media = iMarketingTaskConfigLogic.previewWsMaterialMedia(marketingTaskMediaBO);
        if (Objects.isNull(media)) return BaseResponse.responseOk();
        return BaseResponse.responseOk(media);
    }


}
