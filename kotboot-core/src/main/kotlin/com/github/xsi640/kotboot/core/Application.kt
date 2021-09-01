package com.github.xsi640.kotboot.core

import com.github.xsi640.kotboot.core.plugins.initializeDatabase
import com.github.xsi640.kotboot.core.plugins.json
import com.github.xsi640.kotboot.core.plugins.registerRouting
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }
    install(DefaultHeaders)
    install(CallLogging)
    registerRouting()
    initializeDatabase()
}

fun Application.config(): Config {
    return ConfigFactory.load().getConfig("kotboot")
}