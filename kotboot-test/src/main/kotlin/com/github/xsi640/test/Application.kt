package com.github.xsi640.test

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

data class Message(
    val message: String = "Hello world."
)

interface HelloService {
    fun hello(m: Message): String
}

interface XXXService {
    fun hello(m: Message): String
}

class HelloServiceImpl : HelloService, XXXService {
    override fun hello(m: Message): String {
        return "say ${m.message}"
    }
}

class HelloApplication : KoinComponent {

    // Inject HelloService
    val helloService by inject<HelloService>()

    // display our data
    fun sayHello() = println(helloService.hello(Message("sss")))
}

val helloModule = module {
    single { HelloServiceImpl() as HelloService }
    single { HelloServiceImpl() as XXXService }
}

class Application {
}

fun main() {
    startKoin {
        // use Koin logger
        printLogger()
        // declare modules
        modules(helloModule)
    }

    HelloApplication().sayHello()
}