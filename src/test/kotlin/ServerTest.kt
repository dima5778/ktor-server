package com.directory

import com.directory.domain.models.Employee
import com.directory.domain.repository.EmployeeRepository
import com.directory.plugins.configureSerialization
import com.directory.routes.employeeRoutes
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    private val fakeRepository = object : EmployeeRepository {
        override suspend fun getAllEmployees() = listOf(
            Employee(1, "Test User", "Developer", "+7999", "test@test.com", "IT")
        )

        override suspend fun getEmployeeById(id: Int) =
            Employee(1, "Test User", "Developer", "+7999", "test@test.com", "IT")

        override suspend fun searchEmployees(query: String) = emptyList<Employee>()
    }

    @Test
    fun `test employees endpoint returns 401 without token`() = testApplication {
        application {
            configureSerialization()

            // Явно указываем receiver
            install(Authentication) {
                bearer("firebase-auth") {
                    authenticate { null } // всегда отказывать в этом тесте
                }
            }

            routing {
                employeeRoutes(fakeRepository)
            }
        }

        val response = client.get("/api/employees")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}