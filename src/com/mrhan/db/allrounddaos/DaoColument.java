package com.mrhan.db.allrounddaos;

import java.lang.annotation.*;

/**
 * 注解，使用在实体类的字段<br/>
 * 例如
 *   DaoColument(col='userid',colType=ColumentType.PIRMARYKEY) //id对应这表中的userid ,表中userid是主键
 *   int id;
 *   ///type对应这表中的typeid ,表中typeid是外键 ，根据获取Type类中的表注解来获取参照的表！
 *   ///
 *   DaoColument(col='typeid',colType=ColumentType.FORGINKEY,forginClass=Type.class,forginKey=""typeid)
 *   Type type;
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DaoColument {
    /**
     * 实体类的字段对于的表中字段
     * @return
     */
    String col();
    /**
     * 字段对应表中字段的特征（默认，主键，外键，唯一键)
     * @return
     */
    ColumentType colType() default ColumentType.DEFAULT;

    /**
     * 是否是自动增长
     * @return
     */
    boolean isGrowth() default false;

    /**
     * 字段对应的外键表所对应的实体类，类的信息，当字段类型 colType = FORGINKEY 才会有效（必须）
     * @return
     */
    Class<?> forginClass() default DaoColument.class;
    /**
     * 字段对应的外键表中的字段名，当字段类型 colType = FORGINKEY 才会有效（必须）
     * @return
     */
    String forginKey() default "";

}
