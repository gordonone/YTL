package com.ytl.crm.logic.task.interfaces;


import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.req.exec.TaskActionExecResultSummaryQueryReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.SrCaseRelatedMsgActionInfo;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummary;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummaryPageInit;

import java.util.List;

public interface IMarketingTaskExecResultLogic {

    SrCaseRelatedMsgActionInfo queryCaseRelatedMsgAction(String caseCode);

    TaskActionExecResultSummaryPageInit summaryPageInit(String taskCode);

    List<TaskActionExecResultSummary> queryTaskActionExecSummary(TaskActionExecResultSummaryQueryReq queryReq);

    PageResp<TaskActionExecResultItem> listActionItemExecResult(TaskActionExecResultItemListReq listReq);

}
