package com.github.xsi640.kotboot.core.config

import com.github.xsi640.kotboot.core.inject.InjectProvider
import com.typesafe.config.ConfigBeanFactory
import com.typesafe.config.ConfigFactory
import kotlin.reflect.KClass
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

    override fun isMatch(kClass: KClass<*>): Boolean {
        return kClass.hasAnnotation<Config>()
    }

    override fun <T : Any> create(objClass: KClass<T>, fieldClass: KClass<T>): T {
        val config = fieldClass.findAnnotation<Config>()
        val path = config!!.path
        if (path.isNotEmpty()) {
            return ConfigBeanFactory.create(ApplicationConfig.getConfig(path), fieldClass.java)
        }
        throw IllegalArgumentException("The config's path not empty.")
    }
}