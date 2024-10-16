package com.ytl.crm.logic.dict.impl;


import com.ytl.crm.domain.entity.common.dict.SysDictEntity;
import com.ytl.crm.domain.req.dict.SysDictQueryByTypeReq;
import com.ytl.crm.domain.resp.dict.SysDictDTO;
import com.ytl.crm.logic.dict.interfaces.ISysDictLogic;
import com.ytl.crm.service.interfaces.dict.ISysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictLogicImpl implements ISysDictLogic {

    private final ISysDictService iSysDictService;

    @Override
    public List<SysDictDTO> listByDictType(SysDictQueryByTypeReq req) {
        List<SysDictEntity> dictList = iSysDictService.listByType(req.getDictType());
        return dictList.stream().map(item -> {
            SysDictDTO dto = new SysDictDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
