package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.plugins.scanner.ClassType
import kotlin.reflect.full.primaryConstructor

/**
 * 用于创建Bean
 */
interface BeanFactory {
    fun <T> createBean(classType: ClassType): T
}

class StandardBeanFactory(
    val injectorContext: InjectorContext
) : BeanFactory {
    override fun <T> createBean(classType: ClassType): T {
        return if (classType.singleton) {
            if (injectorContext.cache.containsKey(classType)) {
                injectorContext.cache[classType] as T
            } else {
                buildBean(classType)
            }
        } else {
            buildBean(classType)
        }
    }

    private fun <T> buildBean(classType: ClassType): T {
        val obj = newBean(classType) as T
        if (classType.dependencies.isNotEmpty()) {
            classType.dependencies.forEach { classTypeDependence ->
                if (classTypeDependence.isCollection) {
                    val list = mutableListOf<Any>()
                    classTypeDependence.classType.forEach { ct ->
                        list.add(newBean(ct))
                    }
                    classTypeDependence.member.setter.call(obj, list)
                } else {
                    classTypeDependence.member.setter.call(obj, buildBean(classTypeDependence.classType[0]))
                }
            }
        }
        return obj
    }

    private fun <T> newBean(classType: ClassType): T {
        return injectorContext.cache.computeIfAbsent(classType) {
            val constructor = classType.kClass.primaryConstructor
            if (constructor != null && constructor.parameters.isEmpty()) {
                constructor.call()
            } else {
                throw IllegalArgumentException("Not supported. create ${classType.kClass.simpleName}")
            }
        } as T
    }
}