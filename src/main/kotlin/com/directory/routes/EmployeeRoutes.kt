package com.directory.routes

import com.directory.domain.models.Employee
import com.directory.domain.repository.EmployeeRepository
import com.directory.routes.dto.EmployeeRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.employeeRoutes(repository: EmployeeRepository) {

    route("/employees") {

        get {
            // Получаем userId из Firebase токена
            val userId = call.principal<UserIdPrincipal>()?.name
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            call.respond(repository.getAllEmployees(userId))
        }

        get("/search") {
            val userId = call.principal<UserIdPrincipal>()?.name
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            val query = call.request.queryParameters["q"] ?: ""
            call.respond(repository.searchEmployees(query, userId))
        }

        get("{id}") {
            val userId = call.principal<UserIdPrincipal>()?.name
                ?: return@get call.respond(HttpStatusCode.Unauthorized)

            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Неверный ID")

            val employee = repository.getEmployeeById(id, userId)
                ?: return@get call.respond(HttpStatusCode.NotFound, "Сотрудник не найден")

            call.respond(employee)
        }

        post {
            val userId = call.principal<UserIdPrincipal>()?.name
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

            val request = call.receive<EmployeeRequest>()
            val employee = Employee(
                userId = userId,  // ← привязываем к пользователю
                name = request.name,
                position = request.position,
                phone = request.phone,
                email = request.email,
                department = request.department
            )
            val created = repository.createEmployee(employee)
            call.respond(HttpStatusCode.Created, created)
        }

        put("{id}") {
            val userId = call.principal<UserIdPrincipal>()?.name
                ?: return@put call.respond(HttpStatusCode.Unauthorized)

            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest)

            val request = call.receive<EmployeeRequest>()
            val success = repository.updateEmployee(
                id = id,
                employee = Employee(
                    id = id,
                    userId = userId,
                    name = request.name,
                    position = request.position,
                    phone = request.phone,
                    email = request.email,
                    department = request.department
                ),
                userId = userId
            )
            if (success) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

        delete("{id}") {
            val userId = call.principal<UserIdPrincipal>()?.name
                ?: return@delete call.respond(HttpStatusCode.Unauthorized)

            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            val success = repository.deleteEmployee(id, userId)
            if (success) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}