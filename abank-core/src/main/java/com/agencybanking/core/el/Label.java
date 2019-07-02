package com.agencybanking.core.el;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Gives a fine name recipients field annotated with it
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Label {
    String value();

    String variable() default "";

    boolean embedded() default false;

    String enumListValue() default "";

    String codeListValue() default "";

    Class<?> extension() default Object.class;

}
