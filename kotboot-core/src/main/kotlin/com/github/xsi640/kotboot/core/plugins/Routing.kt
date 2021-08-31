package com.github.xsi640.kotboot.core.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.registerRouting() {

    routing {
        get("/test") {
            call.respond("Hello world")
        }
    }
}
