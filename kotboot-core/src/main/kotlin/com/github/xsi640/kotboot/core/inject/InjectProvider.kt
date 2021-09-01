package com.github.xsi640.kotboot.core.inject

import kotlin.reflect.KClass

interface InjectProvider {
    fun isMatch(kClass: KClass<*>): Boolean
    fun <T : Any> create(objClass: KClass<T>, fieldClass: KClass<T>): T
}