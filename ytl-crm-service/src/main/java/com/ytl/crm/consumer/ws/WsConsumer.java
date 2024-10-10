package com.ytl.crm.consumer.ws;


import com.ytl.crm.domain.req.ws.WsCorpCreateMsgTaskReq;
import com.ytl.crm.domain.req.ws.WsMaterialReq;
import com.ytl.crm.domain.req.ws.WsMsgTaskExecDetailQueryReq;
import com.ytl.crm.domain.resp.ws.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>微盛Consumer</p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author lixs5
 * @version 1.0
 * @date 2024/7/19 09:29
 * @since 1.0
 */
@FeignClient(name = "wsConsumer", url = "${ws.domain:https://open.wshoto.com}")
public interface WsConsumer {

    /**
     * 获取微盛accessToken
     * <a href="https://open.wshoto.com/doc/pages/access_token.html"/a>
     *
     * @param appid      企微管家颁发给开发者的应用ID
     * @param app_secret 企微管家颁发给开发者的应用secret
     */
    @GetMapping("/openapi/access_token")
    @ApiOperation(value = "获取token", httpMethod = "GET", notes = "获取token")
    WsBaseResponse<WsAccessToken> acquireAccessToken(@RequestParam("appid") String appid, @RequestParam("app_secret") String app_secret);

    /**
     * 获取微盛accessToken
     * <a href="https://open.wshoto.com/doc/pages/mass/MassCorpMsgTaskCreate.html"/a>
     *
     * @param req 创建群发任务入参
     */
    @PostMapping("/openapi/mass_send/corp/msg_task/create")
    @ApiOperation(value = "群发任务创建", httpMethod = "POST", notes = "群发任务创建")
    WsBaseResponse<WsCorpCreateMsgTaskResp> corpCreateMsgTask(@RequestParam("access_token") String accessToken, @RequestBody WsCorpCreateMsgTaskReq req);

    /**
     * 根据任务id获取群发任务员工执行情况
     * <a href="https://open.wshoto.com/doc/pages/mass/massTaskExecuteDetail.html"/a>
     *
     * @param req 查询请求
     */
    @PostMapping("/openapi/mass_send/corp/get_mass_task_execute_detail")
    @ApiOperation(value = "查询任务执行情况", httpMethod = "POST", notes = "查询任务执行情况")
    WsBaseResponse<WsMsgTaskExecDetail> queryTaskExecDetail(@RequestParam("access_token") String accessToken, @RequestBody WsMsgTaskExecDetailQueryReq req);


    /**
     * 可获取企业在本应用可见范围内所有员工的信息
     *
     * @param accessToken  token
     * @param currentIndex currentIndex
     * @param pageSize     pageSize
     * @return 员工列表
     */
    @GetMapping("/openapi/department/user/list")
    @ApiOperation(value = "可获取企业在本应用可见范围内所有员工的信息", httpMethod = "GET")
    WsBaseResponse<WsEmpListResp> queryEmpList(@RequestParam("access_token") String accessToken,
                                               @RequestParam("current_index") int currentIndex,
                                               @RequestParam("page_size") int pageSize);

}
