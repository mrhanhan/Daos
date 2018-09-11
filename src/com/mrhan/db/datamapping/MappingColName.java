package com.mrhan.db.datamapping;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingColName {
    String colName() default "";
}
