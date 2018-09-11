package com.mrhan.db.allrounddaos;

import java.lang.annotation.*;


/**
 *注解，使用在实体类！<br/>
 * 表示当前实体类对应需要操作的数据表<br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DaoTable {
    /**
     * 实体类对应的表明
     * @return
     */
    String table();
    /**
     * 实体类Class 类的信息
     * @return
     */
    Class<?> entityClass();

    /**
     * 外键被引用的类
     * @return
     */
    Class<?>[] forginClass() default {};
}
