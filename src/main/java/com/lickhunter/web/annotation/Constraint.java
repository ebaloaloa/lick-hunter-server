package com.lickhunter.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check the constraint of the web service input parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
public @interface Constraint {

    /**
     * <code>true</code> if the parameter can be <code>null</code>. default is <code>false</code>.
     *
     * @return <code>true</code> if the parameter can be <code>null</code>.
     */
    boolean nullable() default false;

    /**
     * <code>true</code> if the list can be empty. default is <code>false</code>.
     *
     * @return <code>true</code> if the list can be empty.
     */
    boolean emptyList() default false;

    /**
     * @return an exampl value of the parameter.
     */
    String example() default "";
}
