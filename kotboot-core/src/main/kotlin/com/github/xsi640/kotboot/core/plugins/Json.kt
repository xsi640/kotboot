package com.github.xsi640.kotboot.core.plugins

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.xsi640.kotboot.core.config.ApplicationConfig
import io.ktor.features.*
import io.ktor.jackson.*
import java.text.SimpleDateFormat
import java.util.*

fun ContentNegotiation.Configuration.json() {
    jackson {
        if (ApplicationConfig.hasPath("jackson")) {
            val jacksonConfig = ApplicationConfig.getConfig("jackson")
            if (jacksonConfig.hasPath("feature")) {
                val featureConfig = jacksonConfig.getConfig("feature")
                featureConfig.entrySet().forEach { e ->
                    val value = featureConfig.getBoolean(e.key)
                    val mapperFeature = MapperFeature.values().firstOrNull { it.name == e.key }
                    if (mapperFeature != null) {
                        if (value) enable(mapperFeature) else disable(mapperFeature)
                    }
                    val serializationFeature = SerializationFeature.values().firstOrNull { it.name == e.key }
                    if (serializationFeature != null) {
                        if (value) enable(serializationFeature) else disable(serializationFeature)
                    }
                    val deserializationFeature = DeserializationFeature.values().firstOrNull { it.name == e.key }
                    if (deserializationFeature != null) {
                        if (value) enable(deserializationFeature) else disable(deserializationFeature)
                    }
                    val jsonParserFeature = JsonParser.Feature.values().firstOrNull { it.name == e.key }
                    if (jsonParserFeature != null) {
                        if (value) enable(jsonParserFeature) else disable(jsonParserFeature)
                    }
                    val jsonGeneratorFeature = JsonGenerator.Feature.values().firstOrNull { it.name == e.key }
                    if (jsonGeneratorFeature != null) {
                        if (value) enable(jsonGeneratorFeature) else disable(jsonGeneratorFeature)
                    }
                }
            }

            if (jacksonConfig.hasPath("dateFormat")) {
                dateFormat = SimpleDateFormat(jacksonConfig.getString("dateFormat"))
            }
            if (jacksonConfig.hasPath("timeZone")) {
                setTimeZone(TimeZone.getTimeZone(jacksonConfig.getString("timeZone")))
            }
        }
    }
}