package com.ytl.crm.service.ws.define.dict;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.common.dict.SysDictEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典项配置表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
public interface ISysDictService extends IService<SysDictEntity> {

    List<SysDictEntity> listByType(String type);
    Map<String,List<SysDictEntity>> MapsByTypes(List<String> types);

}
