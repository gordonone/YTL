package com.ytl.crm.api.channel;


import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.ChannelInfoEntity;
import com.ytl.crm.domain.req.channel.ChannelInfoEditReq;
import com.ytl.crm.domain.req.channel.ChannelInfoSearchReq;
import com.ytl.crm.domain.resp.channel.ChannelInfosResp;
import com.ytl.crm.logic.channel.impl.ChannelInfoLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@RestController
@RequestMapping("/channel/info")
@Api(tags = "渠道信息管理")
public class ChannelInfoAPI {
    @Resource
    private ChannelInfoLogic channelInfoLogic;

    //渠道信息列表
    @PostMapping("/search")
    @ApiOperation(value = "渠道信息列表")
    public BaseResponse<ChannelInfosResp> search(@RequestBody ChannelInfoSearchReq req) {
        return channelInfoLogic.search(req);
    }
    //渠道信息新增、编辑
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "渠道信息新增、编辑")
    public BaseResponse saveOrUpdate(@RequestBody @Valid ChannelInfoEditReq req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseResponse.responseFail(bindingResult.getFieldError().getDefaultMessage());
        }
        return channelInfoLogic.saveOrUpdate(req);
    }
    //渠道信息禁用
    @PostMapping("/setStatus")
    @ApiOperation(value = "渠道信息禁用")
    public BaseResponse setStatus(@RequestBody ChannelInfoEditReq req) {
        return channelInfoLogic.setStatus(req);
    }
    //编辑回显
    @PostMapping("/editSearch")
    @ApiOperation(value = "编辑回显")
    public BaseResponse<ChannelInfosResp> editSearch(@RequestBody ChannelInfoEditReq req) {
        return channelInfoLogic.editSearch(req);
    }

    //渠道信息列表
    @GetMapping("/getByCategoryCode")
    @ApiOperation(value = "根据分类code查询渠道信息")
    public BaseResponse<List<ChannelInfoEntity>> getByCategoryCode(@RequestParam("categoryCode") @ApiParam String categoryCode) {
        return channelInfoLogic.getByCategoryCode(categoryCode);
    }
}
