package com.ytl.crm.domain.bo.channel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "StaffChannelLiveBo", description = "")
public class StaffChannelLiveBo implements Serializable {

    private String logicCode;

    @JsonIgnore
    private String externalUserId;

    @JsonIgnore
    private Long channelCode;

}
