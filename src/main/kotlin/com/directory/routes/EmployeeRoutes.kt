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
            call.respond(repository.getAllEmployees())
        }

        get("/search") {
            val query = call.request.queryParameters["q"] ?: ""
            call.respond(repository.searchEmployees(query))
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Неверный ID")

            val employee = repository.getEmployeeById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound, "Сотрудник не найден")

            call.respond(employee)
        }
        // CREATE
        post {
            val request = call.receive<EmployeeRequest>()
            val employee = Employee(
                name = request.name,
                position = request.position,
                phone = request.phone,
                email = request.email,
                department = request.department
            )
            val created = repository.createEmployee(employee)
            call.respond(HttpStatusCode.Created, created)
        }

        // UPDATE
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest)

            val request = call.receive<EmployeeRequest>()
            val success = repository.updateEmployee(id, Employee(
                id = id,
                name = request.name,
                position = request.position,
                phone = request.phone,
                email = request.email,
                department = request.department
            ))
            if (success) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

        // DELETE
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            val success = repository.deleteEmployee(id)
            if (success) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}