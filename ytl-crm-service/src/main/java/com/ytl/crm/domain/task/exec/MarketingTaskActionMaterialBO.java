package com.ytl.crm.domain.task.exec;

import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createUserCode;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;


    public static MarketingTaskActionMaterialBO assemblyBO(MarketingTaskActionMaterialEntity entity) {
        MarketingTaskActionMaterialBO materialBO = new MarketingTaskActionMaterialBO();
        BeanUtils.copyProperties(entity, materialBO);
        return materialBO;
    }


}
