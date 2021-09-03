package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.scanner.ClassType
import com.github.xsi640.kotboot.core.scanner.ClassTypeDependence
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.*

/**
 * 用于构建依赖关系
 */
interface DependenciesProcessor {
    fun process(classes: Set<ClassType>)
}

class StandardDependenciesProcessor : DependenciesProcessor {

    private val injectProviders = mutableSetOf<InjectProvider>()

    override fun process(classes: Set<ClassType>) {
        val map = mutableMapOf<KClass<*>, ClassType>()
        classes.forEach { classType ->
            if (classType.kClass.isSubclassOf(InjectProvider::class)) {
                injectProviders.add(classType.kClass.primaryConstructor!!.call() as InjectProvider)
            } else {
                map[classType.kClass] = classType
            }
        }
        classes.forEach { c ->
            grunt(c, map)
        }
    }

    private fun grunt(classType: ClassType, map: Map<KClass<*>, ClassType>) {
        val dependencies = mutableListOf<ClassTypeDependence>()
        classType.kClass.declaredMemberProperties.forEach { member ->
            if (member.hasAnnotation<Autowired>()) {
                if (member !is KMutableProperty<*>) {
                    throw IllegalArgumentException("The member can't modify.")
                }
                dependencies.add(findDependenciesClass(member, map))
            } else {
                val injectProvider = injectProviders.firstOrNull { it.isMatch(member) }
                if (injectProvider != null) {
                    if (member !is KMutableProperty<*>) {
                        throw IllegalArgumentException("The member can't modify.")
                    }
                    val type = member.returnType.classifier as KClass<*>
                    val isCollection = type.superclasses.contains(List::class)
                    if (isCollection) {
                        throw IllegalArgumentException("Not support List member.")
                    }
                    dependencies.add(
                        ClassTypeDependence(
                            classType = listOf(ClassType.fromClass(type)),
                            isCollection = false,
                            member = member,
                            injectProvider = injectProvider
                        )
                    )
                }
            }
        }
        classType.dependencies = dependencies
    }

    private fun findDependenciesClass(
        member: KMutableProperty<*>,
        map: Map<KClass<*>, ClassType>
    ): ClassTypeDependence {
        val classTypes = mutableListOf<ClassType>()
        val type = member.returnType.classifier as KClass<*>
        val isCollection = type.isSubclassOf(List::class)
        if (isCollection) {
            val c = member.returnType.arguments[0].type!!.classifier as KClass<*>
            map.keys.forEach {
                if (c.isSuperclassOf(it)) {
                    classTypes.add(map[it]!!)
                }
            }
        } else {
            map.keys.forEach {
                if (it.isSuperclassOf(type)) {
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