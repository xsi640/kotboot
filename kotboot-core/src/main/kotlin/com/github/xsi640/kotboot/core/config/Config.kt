package com.github.xsi640.kotboot.core.config

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Config(
    val path: String = ""
)