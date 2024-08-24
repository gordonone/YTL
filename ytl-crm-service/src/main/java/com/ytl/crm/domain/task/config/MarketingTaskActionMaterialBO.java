package com.ytl.crm.domain.task.config;

import com.alibaba.fastjson.JSON;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

@Data
public class MarketingTaskActionMaterialBO {

    @ApiModelProperty(value = "逻辑编码")
    private String logicCode;

    @ApiModelProperty(value = "任务code，t_c_marketing_task表的logic_code")
    private String taskCode;

    @ApiModelProperty(value = "动作code，t_c_marketing_task_action表的logic_code")
    private String actionCode;

    @ApiModelProperty(value = "素材类型，见枚举")
    private String materialType;

    @ApiModelProperty(value = "素材内容")
    private String materialContent;

    @ApiModelProperty(value = "素材id")
    private String materialId;

    @ApiModelProperty(value = "素材排序")
    private Integer materialOrder;

    @ApiModelProperty(value = "发送方式，见枚举")
    private String sendType;

    @ApiModelProperty(value = "素材实体")
    private MaterialContent contentInfo;

    @Data
    public static class MaterialContent {

        /**
         * 内容 - 纯文本等
         */
        private String content;

        /**
         * 三方素材id
         */
        private String thirdMaterialId;

    }

    public static MarketingTaskActionMaterialBO assemblyBO(MarketingTaskActionMaterialEntity entity) {
        MarketingTaskActionMaterialBO materialBO = new MarketingTaskActionMaterialBO();
        BeanUtils.copyProperties(entity, materialBO);
        String contentJson = entity.getMaterialContent();
        if (!StringUtils.isEmpty(contentJson)) {
            MaterialContent contentInfo = JSON.parseObject(contentJson, MaterialContent.class);
            materialBO.setContentInfo(contentInfo);
        }
        return materialBO;
    }


}
