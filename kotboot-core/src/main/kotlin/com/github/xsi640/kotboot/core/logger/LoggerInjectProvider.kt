package com.github.xsi640.kotboot.core.logger

import com.github.xsi640.kotboot.core.inject.InjectProvider
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Logger

class LoggerInjectProvider : InjectProvider {
    override fun isMatch(property: KProperty<*>): Boolean {
        return property.hasAnnotation<Logger>()
    }

    override fun create(objClass: KClass<*>, property: KProperty<*>): Any {
        return LoggerFactory.getLogger(objClass.java)
    }
}