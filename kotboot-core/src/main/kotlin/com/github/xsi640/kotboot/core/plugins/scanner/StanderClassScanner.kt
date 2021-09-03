package com.github.xsi640.kotboot.core.plugins.scanner

import io.github.classgraph.ClassGraph
import kotlin.reflect.KClass

class StanderClassScanner : ClassScanner {
    override fun scanByAnnotation(packages: Array<String>, annotationClass: KClass<out Annotation>): Set<ClassType> {
        val result = mutableSetOf<ClassType>()
        ClassGraph().enableAllInfo().acceptPackages(*packages).scan().use { scanResult ->
            scanResult.getClassesWithAnnotation(annotationClass.java).forEach { classInfo ->
                result.add(ClassType.fromClass(Class.forName(classInfo.name).kotlin))
            }
        }
        return result
    }

    override fun scanBySuperClass(packages: Array<String>, superClass: KClass<*>): Set<ClassType> {
        val result = mutableSetOf<ClassType>()
        ClassGraph().enableAllInfo().acceptPackages(*packages).scan().use { scanResult ->
            scanResult.getSubclasses(superClass.java).forEach { classInfo ->
                result.add(ClassType.fromClass(Class.forName(classInfo.name).kotlin))
            }
        }
        return result
    }

    override fun scanByInterface(packages: Array<String>, interfaceClass: KClass<*>): Set<ClassType> {
        val result = mutableSetOf<ClassType>()
        ClassGraph().enableAllInfo().acceptPackages(*packages).scan().use { scanResult ->
            scanResult.getClassesImplementing(interfaceClass.java).forEach { classInfo ->
                result.add(ClassType.fromClass(Class.forName(classInfo.name).kotlin))
            }
        }
        return result
    }
}