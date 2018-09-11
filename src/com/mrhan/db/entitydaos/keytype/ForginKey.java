package com.mrhan.db.entitydaos.keytype;

import com.mrhan.db.entitydaos.DataType;

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
     * @return
     */
    Class<?> forginClass();
    /**
     * 外键中对应主表实体类中的字段名(实体类字段)
     * @return
     */
    String fotginCol();

    /**
     * 描述外键存放数据的类型，例如使用数组获取集合存放外键实体数据
     * 或者使用单个对象存放外键数据<hr/>
     *此字段只区别于 {@link DataType} ARRAY 数组<br/>
     * 此字段只区别于 {@link DataType} COLLECTION 集合<br/>
     * 其他类型
     * @return
     */
    DataType forginType() default DataType.INTEGER;

}
