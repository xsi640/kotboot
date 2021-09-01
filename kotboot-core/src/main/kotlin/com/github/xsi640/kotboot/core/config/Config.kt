package com.github.xsi640.kotboot.core.config

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Config(
    val path: String = ""
)