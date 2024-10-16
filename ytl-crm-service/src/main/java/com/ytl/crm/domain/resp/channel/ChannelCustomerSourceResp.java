package com.ytl.crm.domain.resp.channel;


import com.ytl.crm.domain.entity.channel.ChannelInfoEntity;
import com.ytl.crm.domain.resp.channel.dynamic.DynamicColInfoVo;
import com.ytl.crm.domain.resp.channel.dynamic.DynamicDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "客户来源管理返回对象")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelCustomerSourceResp extends BaseResp implements Serializable {
    //动态列信息
    @ApiModelProperty(value = "动态列信息")
    private List<DynamicColInfoVo> dynamicColInfoList;
    //数据
    @ApiModelProperty(value = "数据")
    private List<ChannelCustomerSourceSingle> dataList;

    @ApiModelProperty(value = "编辑回显使用")
    private ChannelCustomerSourceSingle single;

    @ApiModelProperty(value = "渠道信息")
    private ChannelInfoEntity channelInfoEntity;

    @ApiModelProperty(value = "总条数")
    private Long total;
    @Data
    @ApiModel(value = "客户来源管理返回对象单个对象")
    public static class ChannelCustomerSourceSingle implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "渠道id")
        private Long channelInfoId;
        @ApiModelProperty(value = "渠道code")
        private String channelInfoLogicCode;

        @ApiModelProperty(value = "客户名称")
        private String empName;

        @ApiModelProperty(value = "客户企业微信id")
        private String empWxId;

        @ApiModelProperty(value = "状态   1 启用 2 禁用")
        private Integer status;

        @ApiModelProperty(value = "创建时间")
        private Date createTime;

        @ApiModelProperty(value = "创建人编号")
        private String createUserCode;
        @ApiModelProperty(value = "创建人姓名")
        private String createUserName;

        @ApiModelProperty(value = "修改时间")
        private Date modifyTime;

        @ApiModelProperty(value = "修改人编号")
        private String modifyUserCode;

        @ApiModelProperty(value = "修改人姓名")
        private String modifyUserName;

        @ApiModelProperty(value = "关联渠道的名称")
        private String channelInfoName;

        @ApiModelProperty(value = "渠道分类code")
        private String categoryCode;

        @ApiModelProperty(value = "渠道分类全路径名称")
        private String categoryFullName;
        private List<DynamicDataVo> dynamicData;
    }
}
