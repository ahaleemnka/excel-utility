package com.excel.utility.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelMapper {
    String sheetName() default "";
    boolean includeAll() default false;
}
