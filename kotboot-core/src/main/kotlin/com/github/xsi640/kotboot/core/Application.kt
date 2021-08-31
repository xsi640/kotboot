package com.github.xsi640.kotboot.core

import com.github.xsi640.kotboot.core.plugins.initializeDatabase
import com.github.xsi640.kotboot.core.plugins.json
import com.github.xsi640.kotboot.core.plugins.registerRouting
import io.ktor.application.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }
    registerRouting()
    initializeDatabase()
}