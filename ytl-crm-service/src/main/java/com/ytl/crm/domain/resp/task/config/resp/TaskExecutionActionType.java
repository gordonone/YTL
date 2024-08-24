package com.ytl.crm.domain.resp.task.config.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutionActionType {

    @ApiModelProperty(value = "执行动作编码")
    public String code;

    @ApiModelProperty(value = "执行动作描述")
    public String desc;

    @ApiModelProperty(value = "执行动作二级分类")
    public List<TaskExecutionActionType> childrenCatalog;


    public TaskExecutionActionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
