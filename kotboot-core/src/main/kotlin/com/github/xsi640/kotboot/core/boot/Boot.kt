package com.github.xsi640.kotboot.core.boot

import com.github.xsi640.kotboot.core.inject.InjectorContext

interface Boot {
    var injectorContext: InjectorContext

    val order: Int

    fun run()

    fun destroy()
}