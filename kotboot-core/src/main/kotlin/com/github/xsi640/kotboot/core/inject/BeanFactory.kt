package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.scanner.ClassType
import kotlin.reflect.full.primaryConstructor

/**
 * 用于创建Bean
 */
interface BeanFactory {
    fun <T> createBean(classType: ClassType): T
}

class StandardBeanFactory(
    private val cache: MutableMap<ClassType, Any>
) : BeanFactory {
    override fun <T> createBean(classType: ClassType): T {
        return if (classType.singleton) {
            if (cache.containsKey(classType)) {
                cache[classType] as T
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
                if (classTypeDependence.injectProvider == null) {
                    if (classTypeDependence.isCollection) {
                        val list = mutableListOf<Any>()
                        classTypeDependence.classType.forEach { ct ->
                            list.add(newBean(ct))
                        }
                        classTypeDependence.member.setter.call(obj, list)
                    } else {
                        classTypeDependence.member.setter.call(obj, buildBean(classTypeDependence.classType[0]))
                    }
                } else {
                    classTypeDependence.member.setter.call(
                        obj,
                        classTypeDependence.injectProvider.create(
                            classType.kClass,
                            classTypeDependence.member
                        )
                    )
                }
            }
        }
        return obj
    }

    private fun <T> newBean(classType: ClassType): T {
        return cache.computeIfAbsent(classType) {
            val constructor = classType.kClass.primaryConstructor
            if (constructor != null && constructor.parameters.isEmpty()) {
                constructor.call()
            } else {
                throw IllegalArgumentException("Not supported. create ${classType.kClass.simpleName}")
            }
        } as T
    }
}