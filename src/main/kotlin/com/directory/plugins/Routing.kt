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
        get("/") {
            call.respondText("""
                Сервер успешно работает!
                Порт: 8080
                
                Защищённые ссылки (требуют токен):
                - GET /api/employees
                - GET /api/employees/{id}
                - GET /api/employees/search?q=текст
                - POST /api/employees
                - PUT /api/employees/{id}
                - DELETE /api/employees/{id}
            """.trimIndent(), ContentType.Text.Plain)
        }

        authRoutes()

        // Только защищённые маршруты — публичных больше нет
        route("/api") {
            authenticate("firebase-auth") {
                employeeRoutes(repository)
            }
        }
    }
}