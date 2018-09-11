package com.mrhan.db.datamapping;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappingTableName {
    /**
     * 设置表的名称
     * @return
     */
     String tableName() default "";//表明
}
