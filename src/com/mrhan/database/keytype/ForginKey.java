package com.mrhan.database.keytype;

import java.lang.annotation.*;

/**
 * 外键
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForginKey {
    /**
     * 外键引用的实体类的Class 对象
     *
     * @return
     */
    Class<?> entity();

    /**
     * 外键中对应主表实体类中的字段名(实体类字段)
     *
     * @return
     */
    String forginCol() default "";

    /**
     * 判断是否是实体对象！如果是实体对象！则配置的数据类型将无效
     * @return
     */
    boolean isEntity() default false;
}
