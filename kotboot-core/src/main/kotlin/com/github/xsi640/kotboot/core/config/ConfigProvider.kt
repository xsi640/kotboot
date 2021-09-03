package com.github.xsi640.kotboot.core.config

import com.github.xsi640.kotboot.core.inject.InjectProvider
import com.typesafe.config.ConfigBeanFactory
import com.typesafe.config.ConfigFactory
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

val ApplicationConfig: com.typesafe.config.Config =
    ConfigFactory.parseResources("application.conf").resolve().getConfig("kotboot")

class ConfigProvider : InjectProvider {

    companion object {
        inline fun <reified T : Any> load(path: String): T {
            return ConfigBeanFactory.create(ApplicationConfig.getConfig(path), T::class.java)
        }

        fun getString(path: String): String {
            return ApplicationConfig.getString(path)
        }
    }

    override fun isMatch(property: KProperty<*>): Boolean {
        return property.hasAnnotation<Config>()
    }

    override fun create(objClass: KClass<*>, property: KProperty<*>): Any {
        val config = property.findAnnotation<Config>()
        val path = config!!.path
        if (path.isNotEmpty()) {
            val type = property.returnType.classifier as KClass<*>
            return ConfigBeanFactory.create(ApplicationConfig.getConfig(path), type.java)
        }
        throw IllegalArgumentException("The config's path not empty.")
    }
}