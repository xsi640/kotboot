package com.github.xsi640.kotboot.core.logger

import com.github.xsi640.kotboot.core.inject.InjectProvider
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Logger

class LoggerInjectProvider : InjectProvider {
    override fun isMatch(kClass: KClass<*>): Boolean {
        return kClass.hasAnnotation<Logger>()
    }

    override fun <T : Any> create(objClass: KClass<T>, fieldClass: KClass<T>): T {
        return LoggerFactory.getLogger(objClass.java) as T
    }
}