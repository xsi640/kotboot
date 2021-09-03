package com.github.xsi640.kotboot.core.scanner

import com.github.xsi640.kotboot.core.inject.Bean
import com.github.xsi640.kotboot.core.inject.InjectProvider
import com.github.xsi640.kotboot.core.inject.Named
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation

/**
 * 用于扫描注入的类
 */
interface ClassScanner {
    fun scanByAnnotation(packages: Array<String>, annotationClass: KClass<out Annotation>): Set<ClassType>
    fun scanBySuperClass(packages: Array<String>, superClass: KClass<*>): Set<ClassType>
    fun scanByInterface(packages: Array<String>, interfaceClass: KClass<*>): Set<ClassType>
}

data class ClassType(
    val name: String,
    val kClass: KClass<*>,
    val singleton: Boolean,
    var dependencies: List<ClassTypeDependence>
) {
    companion object {
        fun fromClass(kClass: KClass<*>): ClassType {
            val inject = kClass.findAnnotation<Bean>()
            val named = kClass.findAnnotation<Named>()
            return ClassType(
                name = named?.name ?: "",
                kClass = kClass,
                singleton = inject?.singleton ?: true,
                emptyList()
            )
        }
    }
}

data class ClassTypeDependence(
    val classType: List<ClassType>,
    val isCollection: Boolean,
    val member: KMutableProperty<*>,
    val injectProvider: InjectProvider? = null
)