package com.directory.data

import com.directory.db.DatabaseFactory.dbQuery
import com.directory.db.tables.EmployeesTable
import com.directory.domain.models.Employee
import com.directory.domain.repository.EmployeeRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EmployeeRepositoryImpl : EmployeeRepository {
    private fun rowToEmployee(row: ResultRow) = Employee(
        id = row[EmployeesTable.id],
        userId = row[EmployeesTable.userId],
        name = row[EmployeesTable.name],
        position = row[EmployeesTable.position],
        phone = row[EmployeesTable.phone],
        email = row[EmployeesTable.email],
        department = row[EmployeesTable.department]
    )

    override suspend fun getAllEmployees(userId: String): List<Employee> = dbQuery {
        EmployeesTable
            .selectAll()
            .where { EmployeesTable.userId eq userId }
            .map { rowToEmployee(it) }
    }

    override suspend fun getEmployeeById(id: Int, userId: String): Employee? = dbQuery {
        EmployeesTable
            .selectAll()
            .where {
                (EmployeesTable.id eq id) and
                        (EmployeesTable.userId eq userId)
            }
            .map { rowToEmployee(it) }
            .singleOrNull()
    }

    override suspend fun searchEmployees(query: String, userId: String): List<Employee> = dbQuery {
        val lowerQuery = query.lowercase()
        EmployeesTable
            .selectAll()
            .where {
                (EmployeesTable.userId eq userId) and
                        (
                                (EmployeesTable.name.lowerCase() like "%$lowerQuery%") or
                                        (EmployeesTable.position.lowerCase() like "%$lowerQuery%") or
                                        (EmployeesTable.department.lowerCase() like "%$lowerQuery%")
                                )
            }
            .map { rowToEmployee(it) }
    }

    override suspend fun createEmployee(employee: Employee): Employee = dbQuery {
        val id = EmployeesTable.insert {
            it[userId] = employee.userId
            it[name] = employee.name
            it[position] = employee.position
            it[phone] = employee.phone
            it[email] = employee.email
            it[department] = employee.department
        } get EmployeesTable.id

        employee.copy(id = id)
    }

    override suspend fun updateEmployee(id: Int, employee: Employee, userId: String): Boolean = dbQuery {
        EmployeesTable.update({
            (EmployeesTable.id eq id) and
                    (EmployeesTable.userId eq userId)
        }) {
            it[name] = employee.name
            it[position] = employee.position
            it[phone] = employee.phone
            it[email] = employee.email
            it[department] = employee.department
        } > 0
    }

    override suspend fun deleteEmployee(id: Int, userId: String): Boolean = dbQuery {
        EmployeesTable.deleteWhere {
            (EmployeesTable.id eq id) and
                    (EmployeesTable.userId eq userId)
        } > 0
    }
}