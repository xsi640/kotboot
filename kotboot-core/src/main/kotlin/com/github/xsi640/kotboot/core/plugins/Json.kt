package com.github.xsi640.kotboot.core.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.features.*
import io.ktor.jackson.*
import java.text.SimpleDateFormat

val config: Config = ConfigFactory.load()

fun ContentNegotiation.Configuration.json() {
    jackson {
        if (config.hasPath("kotboot.jackson")) {
            val jacksonConfig = config.getConfig("kotboot.jackson")
            if (jacksonConfig.hasPath("feature")) {
                val featureConfig = jacksonConfig.getConfig("feature")
                featureConfig.entrySet().forEach { e ->
                    val feature = SerializationFeature.valueOf(e.key)
                    val value = featureConfig.getBoolean(e.key)
                    if (value) {
                        enable(feature)
                    } else {
                        disable(feature)
                    }
                }
            }
            if (jacksonConfig.hasPath("dateFormat")) {
                dateFormat = SimpleDateFormat(jacksonConfig.getString("dateFormat"))
            }
        }
    }
}