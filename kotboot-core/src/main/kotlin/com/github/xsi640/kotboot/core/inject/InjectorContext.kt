package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.plugins.scanner.ClassType
import kotlin.reflect.KClass

interface InjectorContext {
    val cache: MutableMap<ClassType, Any>

    fun registerAnnotation(annotationClass: KClass<out Annotation>)

    fun registerSuperClass(superClass: KClass<*>)

    fun registerInterface(interfaceClass: KClass<*>)

    fun excluded(it: KClass<*>)

    fun addBean(any: Any)

    fun registerBean(bean: Any)

    fun <T : Any> findBean(kClass: KClass<*>): T

    fun findByAnnotation(annotationClass: KClass<out Annotation>): List<Any>

    fun <T : Any> findByInterface(kClass: KClass<*>): List<T>

    fun <T : Any> findBySuperClass(kClass: KClass<*>): List<T>

    fun initialize()
}

class InjectorContextImpl : InjectorContext {
    override val cache: MutableMap<ClassType, Any> = mutableMapOf()

    override fun registerAnnotation(annotationClass: KClass<out Annotation>) {
        TODO("Not yet implemented")
    }

    override fun registerSuperClass(superClass: KClass<*>) {
        TODO("Not yet implemented")
    }

    override fun registerInterface(interfaceClass: KClass<*>) {
        TODO("Not yet implemented")
    }

    override fun excluded(it: KClass<*>) {
        TODO("Not yet implemented")
    }

    override fun addBean(any: Any) {
        TODO("Not yet implemented")
    }

    override fun registerBean(bean: Any) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> findBean(kClass: KClass<*>): T {
        TODO("Not yet implemented")
    }

    override fun findByAnnotation(annotationClass: KClass<out Annotation>): List<Any> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> findByInterface(kClass: KClass<*>): List<T> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> findBySuperClass(kClass: KClass<*>): List<T> {
        TODO("Not yet implemented")
    }

    override fun initialize() {
        TODO("Not yet implemented")
    }

}