package com.ytl.crm.domain.bo.channel;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "StaffAccountBo", description = "")
public class StaffAccountBo implements Serializable {

    private StaffBaseBo staffBaseBo;
    private List<StaffPlatformChannelBo> staffPlatformChannelBo;

}
