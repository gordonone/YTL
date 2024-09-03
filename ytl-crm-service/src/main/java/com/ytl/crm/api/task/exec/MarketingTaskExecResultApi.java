package com.ytl.crm.api.task.exec;


import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.req.exec.SrCaseRelatedMsgActionQueryReq;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.req.exec.TaskActionExecResultSummaryQueryReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.SrCaseRelatedMsgActionInfo;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummary;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummaryPageInit;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskExecResultLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@Api(value = "任务执行结果API", tags = "任务执行结果API")
@RequestMapping("/task/exec/result")
@RequiredArgsConstructor
public class MarketingTaskExecResultApi {

    private final IMarketingTaskExecResultLogic iMarketingTaskExecResultLogic;

    @PostMapping("/caseRelated/msgAction/query")
    @ApiOperation(value = "查询SR单关联发消息动作")
    public BaseResponse<SrCaseRelatedMsgActionInfo> queryCaseRelatedSendMsgInfo(@RequestBody @Valid SrCaseRelatedMsgActionQueryReq req) {
        return BaseResponse.responseOk(iMarketingTaskExecResultLogic.queryCaseRelatedMsgAction(req.getCaseCode()));
    }

    @GetMapping("/action/summary/pageInit")
    @ApiOperation(value = "任务统计页面初始化信息")
    public BaseResponse<TaskActionExecResultSummaryPageInit> summaryPageInit(@RequestParam("taskCode") String taskCode) {
        return BaseResponse.responseOk(iMarketingTaskExecResultLogic.summaryPageInit(taskCode));
    }

    @PostMapping("/action/summary/query")
    @ApiOperation(value = "任务统计总结查询")
    public BaseResponse<List<TaskActionExecResultSummary>> queryTaskActionExecSummary(@RequestBody @Valid TaskActionExecResultSummaryQueryReq queryReq) {
        return BaseResponse.responseOk(iMarketingTaskExecResultLogic.queryTaskActionExecSummary(queryReq));
    }

    @PostMapping("/action/item/list")
    @ApiOperation(value = "任务执行结果明细列表")
    public BaseResponse<PageResp<TaskActionExecResultItem>> listActionItemExecResult(@RequestBody @Valid TaskActionExecResultItemListReq listReq) {
        return BaseResponse.responseOk(iMarketingTaskExecResultLogic.listActionItemExecResult(listReq));
    }


}
