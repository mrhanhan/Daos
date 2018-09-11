package com.mrhan.database;

import java.lang.annotation.*;

/**
 * 作用于实体的注解，表示，当前实体类所对应的数据库表
 */
@Target(ElementType.TYPE)
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 当前实体操作的表的名称！
     * @return
     */
    String table();
    /**
     * 实体class对象
     * @return
     */
    Class<?> entity();

    /**
     * 表别名
     * @return
     */
    String as() default "";

}
