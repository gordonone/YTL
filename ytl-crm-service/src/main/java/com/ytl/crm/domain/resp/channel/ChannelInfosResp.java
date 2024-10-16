package com.ytl.crm.domain.resp.channel;


import com.ytl.crm.domain.entity.channel.ChannelInfoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value = "ChannelInfosResp", description = "渠道信息列表返回对象")
public class ChannelInfosResp extends BaseResp implements Serializable {
    @ApiModelProperty(value = "渠道信息列表")
    private List<ChannelInfoVo> channelInfos;
    @ApiModelProperty(value = "渠道信息 编辑回显使用")
    private ChannelInfoEntity channelInfo;
    @ApiModelProperty(value = "规则id集合 编辑回显使用")
    private List<Long> ruleIds;

    @ApiModelProperty(value = "总条数 分页使用")
    private Long total;

}
