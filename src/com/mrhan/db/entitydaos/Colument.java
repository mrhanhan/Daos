package com.mrhan.db.entitydaos;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Colument {
    /**
     * 字段映射的字段名称
     * @return
     */
    String col();
    /**
     * 字段数据类型 默认字符串类型
     * @return
     */
    DataType type() default DataType.STRING;
}
