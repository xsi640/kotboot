package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.plugins.scanner.ClassType
import kotlin.reflect.KClass

interface InjectContext {
    val cache: Map<ClassType, Any>

    fun registerAnnotation(annotationClass: KClass<out Annotation>)

    fun registerSuperClass(superClass: KClass<*>)

    fun registerInterface(interfaceClass: KClass<*>)

    fun registerBean(bean: Any)

    fun <T : Any> findBean(kClass: KClass<*>): T

    fun findByAnnotation(annotationClass: KClass<out Annotation>): List<Any>

    fun <T : Any> findByInterface(kClass: KClass<*>): List<T>

    fun <T : Any> findBySuperClass(kClass: KClass<*>): List<T>
}