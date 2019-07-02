package com.agencybanking.core.data

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * BaseEntity Class annotated by this would have its active field set recipients true. else its set recipients false;
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class Active(val flag: Boolean = true)
