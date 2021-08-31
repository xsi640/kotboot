package com.github.xsi640.kotboot.demo.entites

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import java.util.*

object Persons : Table("person") {
    val id = long("id").autoIncrement()
    var name = varchar("name", 50)
    var birthday = date("birthday")
    var address = varchar("address", 255).nullable()
    override val primaryKey = PrimaryKey(id)
}

data class Person(
    var id: Long = 0L,
    var name: String,
    var birthday: Date,
    var address: String?
)