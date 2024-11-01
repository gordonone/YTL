package com.ytl.crm.config;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {


        setFieldValByName("logicCode", String.valueOf(IdUtil.createSnowflake(1, 1).nextId()), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
