package com.github.xsi640.kotboot.core.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.initializeDatabase() {
    val dbType = config.getString("kotboot.db")
    val config = config.getConfig(dbType)
    val properties = Properties()
    config.entrySet().forEach { e ->
        properties.setProperty(e.key, config.getString(e.key))
    }
    val hikariConfig = HikariConfig(properties)
    val ds = HikariDataSource(hikariConfig)
    Database.connect(ds)

    transaction {
//        SchemaUtils.create()
    }
}