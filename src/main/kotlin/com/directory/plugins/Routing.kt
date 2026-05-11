package com.directory.plugins

import com.directory.data.EmployeeRepositoryImpl
import com.directory.routes.authRoutes
import com.directory.routes.employeeRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "Ошибка сервера: ${cause.message}",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    val repository = EmployeeRepositoryImpl()

    routing {
        // Главная страница
        get("/") {
            call.respondText("""
                ✅ Сервер успешно работает!
                Порт: 8080
                
                Публичные ссылки:
                - GET /public/employees
                - GET /public/employees/search?q=Иван
                
                Защищённые ссылки (требуют токен):
                - GET /api/employees
            """.trimIndent(), ContentType.Text.Plain)
        }
        authRoutes()

        route("/public") {
            get("/employees") {
                val employees = repository.getAllEmployees()
                call.respond(employees)
            }

            get("/employees/search") {
                val query = call.request.queryParameters["q"] ?: ""
                val employees = repository.searchEmployees(query)
                call.respond(employees)
            }
        }

        // Защищённые маршруты (с авторизацией Firebase)
        route("/api") {
            authenticate("firebase-auth") {
                employeeRoutes(repository)
            }
        }
    }
}