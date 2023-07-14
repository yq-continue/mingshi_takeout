package com.atmingshi.common;

import com.atmingshi.utils.TransferId;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 实现公共字段填充
 */
@Component
@Slf4j
public class PublicFieldFill implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {//插入时自动填充
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", TransferId.gteId());
        metaObject.setValue("updateUser", TransferId.gteId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {//修改时自动填充
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",TransferId.gteId());
    }
}
