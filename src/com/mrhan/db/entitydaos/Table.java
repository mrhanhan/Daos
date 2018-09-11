package com.mrhan.db.entitydaos;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 映射的表
     * @return
     */
    String table();
    /**
     * 实体类的Class信息
     * @return
     */
    Class<?> entityClass ();
}
