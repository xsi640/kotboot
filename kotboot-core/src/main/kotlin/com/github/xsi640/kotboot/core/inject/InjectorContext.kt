package com.github.xsi640.kotboot.core.inject

import com.github.xsi640.kotboot.core.NotFoundBeanException
import com.github.xsi640.kotboot.core.plugins.scanner.ClassScanner
import com.github.xsi640.kotboot.core.plugins.scanner.ClassType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf


interface InjectorContext {
    val cache: MutableMap<ClassType, Any>

    fun registerAnnotation(annotationClass: KClass<out Annotation>)

    fun registerSuperClass(superClass: KClass<*>)

    fun registerInterface(interfaceClass: KClass<*>)

    fun excluded(it: KClass<*>)

    fun addBean(any: Any)

    fun <T : Any> findBean(kClass: KClass<*>): T

    fun findByAnnotation(annotationClass: KClass<out Annotation>): List<Any> {
        return findByAnnotation(annotationClass, "")
    }

    fun findByAnnotation(annotationClass: KClass<out Annotation>, name: String): List<Any>

    fun <T : Any> findByInterface(kClass: KClass<*>, name: String): List<T>

    fun <T : Any> findByInterface(kClass: KClass<*>): List<T> {
        return findByInterface(kClass, "")
    }

    fun <T : Any> findBySuperClass(kClass: KClass<*>): List<T> {
        return findBySuperClass(kClass, "")
    }

    fun <T : Any> findBySuperClass(kClass: KClass<*>, name: String): List<T>

    fun initialize()
}

class InjectorContextImpl(
    val packages: Array<String>,
    val classScanner: ClassScanner,
    val dependenciesProcessor: DependenciesProcessor,
    val beanFactory: BeanFactory,
    override val cache: MutableMap<ClassType, Any> = mutableMapOf()
) : InjectorContext {
    private var initialized = false

    private val classTypeCached = mutableMapOf<KClass<*>, ClassType>()
    private val regAnnotations = mutableListOf<KClass<out Annotation>>()
    private val regSuperClasses = mutableListOf<KClass<*>>()
    private val regInterfaceClasses = mutableListOf<KClass<*>>()
    private val excludedClasses = mutableListOf<KClass<*>>()

    override fun registerAnnotation(annotationClass: KClass<out Annotation>) {
        regAnnotations.add(annotationClass)
    }

    override fun registerSuperClass(superClass: KClass<*>) {
        regSuperClasses.add(superClass)
    }

    override fun registerInterface(interfaceClass: KClass<*>) {
        regInterfaceClasses.add(interfaceClass)
    }

    override fun excluded(it: KClass<*>) {
        excludedClasses.add(it)
    }

    override fun addBean(any: Any) {
        val classType = ClassType.fromClass(any::class)
        classTypeCached[any::class] = classType
        cache[classType] = any
    }

    override fun <T : Any> findBean(kClass: KClass<*>): T {
        if (!initialized) {
            throw IllegalStateException("waiting initialized.")
        }
        if (!classTypeCached.containsKey(kClass)) {
            throw NotFoundBeanException("Not found Bean class. ${kClass.simpleName}")
        } else {
            val classType = classTypeCached[kClass]!!
            return beanFactory.createBean(classType) as T
        }
    }

    override fun findByAnnotation(annotationClass: KClass<out Annotation>, name: String): List<Any> {
        if (!initialized) {
            throw IllegalStateException("waiting initialized.")
        }
        val result = mutableListOf<Any>()
        classTypeCached.forEach { (k, _) ->
            if (k.annotations.any { it.annotationClass == annotationClass }) {
                if (name.isEmpty()) {
                    result.add(this.findBean(k))
                } else {
                    val classType = classTypeCached[annotationClass]
                    if (classType != null && name == classType.name) {
                        result.add(findBean(k))
                    }
                }
            }
        }
        return result;
    }

    override fun <T : Any> findByInterface(kClass: KClass<*>, name: String): List<T> {
        if (!initialized) {
            throw IllegalStateException("waiting initialized.")
        }
        val result = mutableListOf<T>()
        classTypeCached.keys.forEach { k ->
            if (kClass.isSuperclassOf(k)) {
                if (name.isEmpty()) {
                    result.add(findBean(k))
                } else {
                    val classType = classTypeCached[k]
                    if (classType != null && classType.name == name) {
                        result.add(findBean(k))
                    }
                }
            }
        }
        return result
    }

    override fun <T : Any> findBySuperClass(kClass: KClass<*>, name: String): List<T> {
        if (!initialized) {
            throw IllegalStateException("waiting initialized.")
        }
        val result = mutableListOf<T>()
        classTypeCached.keys.forEach { k ->
            if (kClass.isSuperclassOf(k)) {
                if (name.isEmpty()) {
                    result.add(findBean(k))
                } else {
                    val classType = classTypeCached[k]
                    if (classType != null && classType.name == name) {
                        result.add(findBean(k))
                    }
                }
            }
        }
        return result
    }

    override fun initialize() {
        classTypeCached.clear()
        val set = mutableSetOf<ClassType>()
        regAnnotations.forEach {
            set.addAll(classScanner.scanByAnnotation(packages, it))
        }
        regSuperClasses.forEach {
            set.addAll(classScanner.scanBySuperClass(packages, it))
        }
        regInterfaceClasses.forEach {
            set.addAll(classScanner.scanByInterface(packages, it))
        }

        dependenciesProcessor.process(set)
        set.forEach { classType ->
            if (excludedClasses.none { it == classType.kClass }) {
                classTypeCached[classType.kClass] = classType
            }
        }
        initialized = true;
    }

}