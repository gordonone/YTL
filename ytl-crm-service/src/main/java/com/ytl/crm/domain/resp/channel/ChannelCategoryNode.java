package com.ytl.crm.domain.resp.channel;

import com.ziroom.ugc.crm.service.web.domain.entity.channel.ChannelCategoryTreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="渠道分类返回实体", description="")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelCategoryNode implements Serializable {
    @ApiModelProperty(value = "当前节点")
    private ChannelCategoryTreeEntity curNode;
    @ApiModelProperty(value = "子节点")
    private List<ChannelCategoryNode> children;
}
