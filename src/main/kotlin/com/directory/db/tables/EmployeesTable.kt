package com.directory.db.tables

import org.jetbrains.exposed.sql.Table

object EmployeesTable : Table("employees") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val position = varchar("position", 255)
    val phone = varchar("phone", 50)
    val email = varchar("email", 255)
    val department = varchar("department", 255)

    override val primaryKey = PrimaryKey(id)
}