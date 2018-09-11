package com.mrhan.db.entitydaos.keytype;

import java.lang.annotation.*;

/**
 * 自动增长键
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoKey {

}
