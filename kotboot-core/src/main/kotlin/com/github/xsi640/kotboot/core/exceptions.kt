package com.github.xsi640.kotboot.core

class NotFoundBeanException(
    override val message: String
) : RuntimeException(message)