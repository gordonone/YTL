package com.ytl.crm.api.channel;



import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.ChannelCategoryTreeEntity;
import com.ytl.crm.domain.req.channel.ChannelCategoryAddReq;
import com.ytl.crm.domain.req.channel.ChannelCategoryEditReq;
import com.ytl.crm.domain.req.channel.ChannelCategoryReq;
import com.ytl.crm.domain.resp.channel.ChannelCategoryNode;
import com.ytl.crm.domain.resp.channel.ChannelOpLogsResp;
import com.ytl.crm.logic.channel.impl.ChannelCategoryLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@RestController
@RequestMapping("/channel/category")
@Api(tags = "渠道分类管理")
public class ChannelCategoryTreeAPI {
    @Resource
    ChannelCategoryLogic channelCategoryLogic;

    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有渠道分类", notes = "获取所有渠道分类")
    public BaseResponse<List<ChannelCategoryNode>> getAll() {
        return channelCategoryLogic.getAll();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增渠道分类", notes = "新增渠道分类")
    public BaseResponse<ChannelCategoryTreeEntity> add(@RequestBody @Valid ChannelCategoryAddReq req) {
        return channelCategoryLogic.add(req);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改渠道分类", notes = "修改渠道分类")
    public BaseResponse update(@RequestBody @Valid ChannelCategoryEditReq req) {
        return channelCategoryLogic.update(req);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除渠道分类", notes = "删除渠道分类")
    public BaseResponse delete(@RequestBody @Valid ChannelCategoryReq req) {
        return channelCategoryLogic.delete(req);
    }

    @PostMapping("/setDisable")
    @ApiOperation(value = "设置渠道分类状态", notes = "设置渠道分类状态")
    public BaseResponse setDisable(@RequestBody @Valid ChannelCategoryReq req) {
        return channelCategoryLogic.setDisable(req);
    }

    @GetMapping("/getByCategoryCode")
    @ApiOperation(value = "获取渠道分类操作日志", notes = "获取渠道分类操作日志")
    public BaseResponse<ChannelOpLogsResp> getByCategoryCode(@RequestParam("categoryCode") String categoryCode) {
        return channelCategoryLogic.getByCategoryCode(categoryCode);
    }
}
