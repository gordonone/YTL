package com.ytl.crm.domain.enums.channel;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ChannelStaffCodeTypeEnum implements EnumWithCodeAndDesc<String> {

    SingleCustomer("MULT_CHANNEL", "一客一码"), MultCustomer("SINGLE_CHANNEL", "多客一码");


    private final String code;
    private final String desc;
}
