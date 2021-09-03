package com.github.xsi640.kotboot.core

import com.github.xsi640.kotboot.core.boot.ApplicationContext
import com.github.xsi640.kotboot.core.inject.Bean
//import com.github.xsi640.kotboot.core.plugins.initializeDatabase
import com.github.xsi640.kotboot.core.plugins.json
import com.github.xsi640.kotboot.core.plugins.registerRouting
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.features.*

//fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
//
//fun Application.module(testing: Boolean = false) {
//    install(ContentNegotiation) {
//        json()
//    }
//    install(DefaultHeaders)
//    install(CallLogging)
//    registerRouting()
//    initializeDatabase()
//}
//
//fun Application.config(): Config {
//    return ConfigFactory.load().getConfig("kotboot")
//}
class Application {

}

@Bean
class AAA {
    val s = "aaa"
}

fun main(args: Array<String>) {
    val ctx = ApplicationContext(arrayOf("com.github.xsi640.kotboot"))
    ctx.run()
    val s = ctx.injectorContext.findBean<AAA>(AAA::class)
    println(s.s)
}