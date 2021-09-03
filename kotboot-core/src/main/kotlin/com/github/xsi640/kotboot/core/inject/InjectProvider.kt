package com.github.xsi640.kotboot.core.inject

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface InjectProvider {
    fun isMatch(member: KProperty<*>): Boolean
    fun create(objClass: KClass<*>, property: KProperty<*>): Any
}