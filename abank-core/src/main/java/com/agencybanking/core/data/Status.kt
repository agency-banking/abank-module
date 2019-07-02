package com.agencybanking.core.data

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * List of statuses supported for this product
 *
 * @author dubic
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class Status(val list: Array<KClass<*>>)
