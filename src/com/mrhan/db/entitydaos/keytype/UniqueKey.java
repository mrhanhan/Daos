package com.mrhan.db.entitydaos.keytype;

import java.lang.annotation.*;

/***
 * 唯一键
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueKey {

}
