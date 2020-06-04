package com.android.messagebusexample.lib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface To {
    String value();

    String action() default "";

    Priority withPriority() default Priority.NORMAL;
}
