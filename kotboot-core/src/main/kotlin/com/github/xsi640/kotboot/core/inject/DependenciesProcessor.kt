package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.plugins.scanner.ClassType
import com.github.xsi640.kotboot.core.plugins.scanner.ClassTypeDependence
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.superclasses

/**
 * 用于构建依赖关系
 */
interface DependenciesProcessor {
    fun process(classes: Set<ClassType>)
}

class StandardDependenciesProcessor : DependenciesProcessor {
    override fun process(classes: Set<ClassType>) {
        val map = classes.associateBy { it.kClass }
        classes.forEach { c ->
            grunt(c, map)
        }
    }

    private fun grunt(classType: ClassType, map: Map<KClass<*>, ClassType>) {
        val dependencies = mutableListOf<ClassTypeDependence>()
        classType.kClass.declaredMemberProperties.forEach { member ->
            if (member.findAnnotation<Autowired>() != null) {
                if (member !is KMutableProperty<*>) {
                    throw IllegalArgumentException("The member can't modify.")
                }
                dependencies.add(findDependenciesClass(classType, member, map))
            }
        }
        classType.dependencies = dependencies
    }

    private fun findDependenciesClass(
        classType: ClassType,
        member: KMutableProperty<*>,
        map: Map<KClass<*>, ClassType>
    ): ClassTypeDependence {
        val classTypes = mutableListOf<ClassType>()
        val type = member.returnType.classifier as KClass<*>
        val isCollection = type.superclasses.contains(List::class)
        if (classTypes.isEmpty()) {
            throw IllegalArgumentException("Not found dependence class. ${classType.name}.${member.name}")
        }
        if (isCollection) {
            val c = member.returnType.arguments[0].type!!.classifier as KClass<*>
            map.keys.forEach {
                if (c.superclasses.contains(type)) {
                    classTypes.add(map[it]!!)
                }
            }
        } else {
            map.keys.forEach {
                if (it.superclasses.contains(type)) {
                    classTypes.add(map[it]!!)
                }
            }
        }
        return ClassTypeDependence(
            classType = classTypes,
            isCollection = isCollection,
            member = member
        )
    }
}