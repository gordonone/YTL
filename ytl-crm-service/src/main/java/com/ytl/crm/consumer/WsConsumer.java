package com.ytl.crm.consumer;

import com.ytl.crm.domain.req.ws.WsApplyQrCodeReq;
import com.ytl.crm.domain.req.ws.WsCorpCreateMsgTaskReq;
import com.ytl.crm.domain.req.ws.WsMsgTaskExecDetailQueryReq;
import com.ytl.crm.domain.req.ws.WsQrCodeDetailReq;
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
@FeignClient(name = "wsConsumer", url = "${domain.ws:https://open.wshoto.com}")
public interface WsConsumer {

    /**
     * 获取微盛accessToken
     * https://open.wshoto.com/doc/pages/access_token.html
     *
     * @param appid      企微管家颁发给开发者的应用ID
     * @param app_secret 企微管家颁发给开发者的应用secret
     */
    @GetMapping("/openapi/access_token")
    @ApiOperation(value = "获取token", httpMethod = "GET", notes = "获取token")
    WsBaseResponse<WsAccessToken> acquireAccessToken(@RequestParam("appid") String appid,
                                                     @RequestParam("app_secret") String app_secret);


    /**
     * 申请活码
     *
     * @param accessToken token
     * @param req         查询入参
     * @return 结果
     */
    @PostMapping("/openapi/qrcode/staff/v2/add")
    WsBaseResponse<String> applyQrCode(@RequestParam("access_token") String accessToken, @RequestBody WsApplyQrCodeReq req);

    /**
     * 查询活码详情
     *
     * @param accessToken token
     * @param req         查询入参
     * @return 活码详情
     */
    @PostMapping("/openapi/qrcode/staff_qr_code/detail")
    WsBaseResponse<WsQrCodeDetailResp> queryQrCodeDetail(@RequestParam("access_token") String accessToken, @RequestBody WsQrCodeDetailReq req);

    /**
     * 获取用户列表
     *
     * @param accessToken  token
     * @param currentIndex currentIndex
     * @param pageSize     pageSize
     * @return 结果
     */
    @GetMapping("/openapi/department/user/list?access_token={accessToken}&current_index={currentIndex}&page_size={pageSize}")
    @ApiOperation(value = "可获取企业在本应用可见范围内所有员工的信息", httpMethod = "GET")
    WsBaseResponse<WsEmpDetailResp> getUserList(@RequestParam("access_token") String accessToken,
                                                @RequestParam("current_index") int currentIndex, @RequestParam("page_size") int pageSize);


    /**
     * 获取客户群聊列表
     *
     * @param accessToken      调用接口凭证
     * @param userid           企业成员的userid,userid不能与external_user_id同时传入
     * @param user_type        userid对应企业成员在群内的身份 0:群主 1: 群成员 默认值0
     * @param external_user_id 群成员的外部联系人id,userid不能与external_user_id同时传入
     * @param chat_id          群id
     * @param current_index    页码。默认值为1
     * @param page_size        列表返回长度。默认值为20，最大值为100
     * @return
     */
    @GetMapping("/openapi/chat/group/list")
    @ApiOperation(value = "获取客户群聊列表", httpMethod = "POST")
    WsBaseResponse<WsPageResult<WsGroupInfo>> queryChatGroupList(@RequestParam("access_token") String accessToken,
                                                                 @RequestParam("userid") String userid,
                                                                 @RequestParam("user_type") String user_type,
                                                                 @RequestParam("external_user_id") String external_user_id,
                                                                 @RequestParam("chat_id") String chat_id,
                                                                 @RequestParam(name = "current_index", defaultValue = "1") Integer current_index,
                                                                 @RequestParam(name = "page_size", defaultValue = "20") Integer page_size);


    /**
     * 查询用户详情
     *
     * @param accessToken    token
     * @param externalUserId 用户标识
     * @return 用户详情
     */
    @GetMapping("/openapi/customer/info/detail")
    WsBaseResponse<WsUserDetailResp> getUseDetail(@RequestParam("access_token") String accessToken, @RequestParam("external_userid") String externalUserId);



    /**
     * 获取微盛accessToken
     * <a href="https://open.wshoto.com/doc/pages/mass/MassCorpMsgTaskCreate.html"/a>
     *
     * @param req 创建群发任务入参
     */
    @PostMapping("/openapi/mass_send/corp/msg_task/create")
    @ApiOperation(value = "群发任务创建", httpMethod = "POST", notes = "群发任务创建")
    WsBaseResponse<WsCorpCreateMsgTaskResp> corpCreateMsgTask(@RequestParam("access_token") String accessToken,
                                                              @RequestBody WsCorpCreateMsgTaskReq req);

    /**
     * 根据任务id获取群发任务员工执行情况
     * <a href="https://open.wshoto.com/doc/pages/mass/massTaskExecuteDetail.html"/a>
     *
     * @param req 查询请求
     */
    @PostMapping("/openapi/mass_send/corp/get_mass_task_execute_detail")
    @ApiOperation(value = "查询任务执行情况", httpMethod = "POST", notes = "查询任务执行情况")
    WsBaseResponse<WsMsgTaskExecDetail> queryTaskExecDetail(@RequestParam("access_token") String accessToken,
                                                            @RequestBody WsMsgTaskExecDetailQueryReq req);
}
