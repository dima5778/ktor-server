package com.directory

import com.directory.db.DatabaseFactory
import com.directory.db.tables.EmployeesTable
import com.directory.plugins.configureSecurity
import com.directory.plugins.configureSerialization
import com.directory.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(this)
    configureSerialization()
    configureSecurity()
    configureRouting()
}
