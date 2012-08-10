package org.d2j.utils.database.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 11:36
 * IDE : IntelliJ IDEA
 */
@Retention(value= RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface Static {
}
