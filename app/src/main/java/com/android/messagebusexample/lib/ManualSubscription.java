package com.android.messagebusexample.lib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ManualSubscription should be used in case if no registration is required on application start
 * in this case will not warning log message for right registrations
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ManualSubscription {
}
