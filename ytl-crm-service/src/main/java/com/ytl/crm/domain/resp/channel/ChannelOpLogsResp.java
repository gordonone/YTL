package com.ytl.crm.domain.resp.channel;

import com.ytl.crm.domain.entity.channel.ChannelOpLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelOpLogsResp extends BaseResp implements Serializable {
    private List<ChannelOpLogEntity> logs;
}
