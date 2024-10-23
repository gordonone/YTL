package com.ytl.crm.api.channel;



import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.req.channel.dynamic.DynamicEditReq;
import com.ytl.crm.domain.req.channel.dynamic.DynamicSearchReq;
import com.ytl.crm.domain.resp.channel.ChannelCustomerSourceResp;
import com.ytl.crm.logic.channel.impl.ChannelCustomerSourceLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@RestController
@RequestMapping("/channel/customer/source")
@Api(tags = "客户来源管理")
public class ChannelCustomerSourceAPI {
    @Resource
    private ChannelCustomerSourceLogic channelCustomerSourceLogic;
    //客户动态列表查询
    @PostMapping("/search")
    @ApiOperation(value = "客户动态列表查询")
    public BaseResponse<ChannelCustomerSourceResp> search(@RequestBody DynamicSearchReq req) {
        return channelCustomerSourceLogic.search(req);
    }
    //客户动态表单回显
    @GetMapping("/editBackShow")
    @ApiOperation(value = "客户动态表单回显")
    public BaseResponse<ChannelCustomerSourceResp> editBackShow(@RequestParam(value = "id",required = false) Long id,@RequestParam("channelInfoId") Long channelInfoId) {
        return channelCustomerSourceLogic.editBackShow(id,channelInfoId);
    }
    //客户动态表单保存
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "客户动态表单保存")
    public BaseResponse saveOrUpdate(@RequestBody @Valid DynamicEditReq req) {
        return channelCustomerSourceLogic.saveOrUpdate(req);
    }
    //删除
    @PostMapping("/setStatus")
    public BaseResponse setStatus(@RequestBody  DynamicEditReq req) {
        return channelCustomerSourceLogic.setStatus(req);
    }
}
