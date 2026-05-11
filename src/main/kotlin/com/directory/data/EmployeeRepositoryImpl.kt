package com.directory.data

import com.directory.db.DatabaseFactory.dbQuery
import com.directory.db.tables.EmployeesTable
import com.directory.domain.models.Employee
import com.directory.domain.repository.EmployeeRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

class EmployeeRepositoryImpl : EmployeeRepository {

    override suspend fun getAllEmployees(): List<Employee> = dbQuery {
        EmployeesTable
            .selectAll()
            .map { row ->
                Employee(
                    id = row[EmployeesTable.id],
                    name = row[EmployeesTable.name],
                    position = row[EmployeesTable.position],
                    phone = row[EmployeesTable.phone],
                    email = row[EmployeesTable.email],
                    department = row[EmployeesTable.department]
                )
            }
    }

    override suspend fun getEmployeeById(id: Int): Employee? = dbQuery {
        EmployeesTable
            .selectAll()
            .where { EmployeesTable.id eq id }
            .map { row ->
                Employee(
                    id = row[EmployeesTable.id],
                    name = row[EmployeesTable.name],
                    position = row[EmployeesTable.position],
                    phone = row[EmployeesTable.phone],
                    email = row[EmployeesTable.email],
                    department = row[EmployeesTable.department]
                )
            }.singleOrNull()
    }

    override suspend fun searchEmployees(query: String): List<Employee> = dbQuery {
        EmployeesTable
            .selectAll()
            .where {
                (EmployeesTable.name like "%$query%") or
                        (EmployeesTable.position like "%$query%") or
                        (EmployeesTable.department like "%$query%")
            }
            .map { row ->
                Employee(
                    id = row[EmployeesTable.id],
                    name = row[EmployeesTable.name],
                    position = row[EmployeesTable.position],
                    phone = row[EmployeesTable.phone],
                    email = row[EmployeesTable.email],
                    department = row[EmployeesTable.department]
                )
            }
    }

    override suspend fun createEmployee(employee: Employee): Employee = dbQuery {
        val id = EmployeesTable.insert {
            it[name] = employee.name
            it[position] = employee.position
            it[phone] = employee.phone
            it[email] = employee.email
            it[department] = employee.department
        } get EmployeesTable.id

        employee.copy(id = id)
    }

    override suspend fun updateEmployee(id: Int, employee: Employee): Boolean = dbQuery {
        EmployeesTable.update({ EmployeesTable.id eq id }) {
            it[name] = employee.name
            it[position] = employee.position
            it[phone] = employee.phone
            it[email] = employee.email
            it[department] = employee.department
        } > 0
    }

    override suspend fun deleteEmployee(id: Int): Boolean = dbQuery {
        EmployeesTable.deleteWhere { EmployeesTable.id eq id } > 0
    }
}