package com.mrhan.database;

import com.mrhan.database.mrhanDaos.ColumType;

import java.lang.annotation.*;

/**
 * 作用于实体的注解，表示，当前实体类所对应的数据库表
 */
@Target(ElementType.FIELD)
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Colum {
    /**
     * 对应隐射的字段名称
     *
     * @return
     */
    String col() default "";
    /**
     * 字段数据类型！
     *
     * @return
     */
    ColumType type() default ColumType.AUTO;
    /**
     * 是否是自动增长列
     */
    boolean isAuto() default false;
    /**是否为空*/
    boolean isNull() default true;

    /**
     * 当前字段所属表
     * @return
     */
    String table() default "";
}
