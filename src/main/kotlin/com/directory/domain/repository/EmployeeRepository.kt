package com.directory.domain.repository

import com.directory.domain.models.Employee

interface EmployeeRepository {
    suspend fun getAllEmployees(): List<Employee>
    suspend fun getEmployeeById(id: Int): Employee?
    suspend fun searchEmployees(query: String): List<Employee>

    suspend fun createEmployee(employee: Employee): Employee
    suspend fun updateEmployee(id: Int, employee: Employee): Boolean
    suspend fun deleteEmployee(id: Int): Boolean
}