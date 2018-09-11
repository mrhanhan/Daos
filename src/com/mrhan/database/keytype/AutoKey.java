package com.mrhan.database.keytype;


import java.lang.annotation.*;

/**
 * 外键
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoKey {
}
