package com.mrhan.database.keytype;

import java.lang.annotation.*;

/**
 * 主键
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
}
