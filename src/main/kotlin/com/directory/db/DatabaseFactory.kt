package com.directory.db

import com.directory.db.tables.EmployeesTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(application: Application) {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"

            jdbcUrl = System.getenv("DATABASE_URL")
                ?: application.environment.config.propertyOrNull("database.url")?.getString()
                        ?: "jdbc:postgresql://ep-misty-block-apwh1ut8-pooler.c-7.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require"

            username = System.getenv("DATABASE_USER") ?: "neondb_owner"
            password = System.getenv("DATABASE_PASSWORD") ?: "npg_9WlE2pFXHfrM"

            maximumPoolSize = 5
            minimumIdle = 1
            idleTimeout = 300000
            maxLifetime = 600000
            connectionTimeout = 30000

            // Важные параметры для Neon
            addDataSourceProperty("sslMode", "require")
            addDataSourceProperty("channelBinding", "require")
            addDataSourceProperty("socketTimeout", "30")
            addDataSourceProperty("connectTimeout", "30")
        }

        try {
            val dataSource = HikariDataSource(config)
            Database.connect(dataSource)

            transaction {
                SchemaUtils.create(EmployeesTable)
                seedDatabase()
            }

            println("Успешное подключение к Neon PostgreSQL!")

        } catch (e: Exception) {
            println("Ошибка подключения к Neon:")
            e.printStackTrace()
            throw e
        }
    }

    private fun seedDatabase() {
        if (EmployeesTable.selectAll().count() == 0L) {
            val employees = listOf(
                Triple("Иван Иванов", "Разработчик", "+7 (999) 123-45-67"),
                Triple("Мария Петрова", "Дизайнер", "+7 (999) 234-56-78"),
                Triple("Алексей Сидоров", "Менеджер", "+7 (999) 345-67-89"),
                Triple("Елена Козлова", "Тестировщик", "+7 (999) 456-78-90"),
                Triple("Дмитрий Новиков", "DevOps", "+7 (999) 567-89-01"),
                Triple("Ольга Морозова", "Аналитик", "+7 (999) 678-90-12"),
            )

            employees.forEach { (name, position, phone) ->
                EmployeesTable.insert {
                    it[EmployeesTable.name] = name
                    it[EmployeesTable.position] = position
                    it[EmployeesTable.phone] = phone
                    it[EmployeesTable.email] = "${name.split(" ")[0].lowercase()}@company.com"
                    it[EmployeesTable.department] = "IT"
                }
            }
            println("Database seeded with sample data")
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}