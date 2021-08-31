package com.github.xsi640.kotboot.demo.plugins.routing

import com.github.xsi640.kotboot.demo.entites.Person
import com.github.xsi640.kotboot.demo.entites.Persons
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

fun Application.registerRouting() {

    routing {
        get("/person") {
            val persons = transaction {
                Persons.selectAll().map {
                    Person(
                        id = it[Persons.id],
                        name = it[Persons.name],
                        birthday = it[Persons.birthday].toDate(),
                        address = it[Persons.address]
                    )
                }
            }
            call.respond(persons)
        }

        post("/person") {
            val p = call.receive<Person>()
            val person = transaction {
                Persons.insert {
                    it[name] = p.name
                    it[birthday] = DateTime(p.birthday.time)
                    it[address] = p.address
                }
            }
            p.id = person[Persons.id]
            call.respond(p)
        }
    }
}
