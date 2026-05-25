package com.directory.domain.repository

import com.directory.domain.models.Employee

interface EmployeeRepository {
    suspend fun getAllEmployees(userId: String): List<Employee>
    suspend fun getEmployeeById(id: Int, userId: String): Employee?
    suspend fun searchEmployees(query: String, userId: String): List<Employee>
    suspend fun createEmployee(employee: Employee): Employee
    suspend fun updateEmployee(id: Int, employee: Employee, userId: String): Boolean
    suspend fun deleteEmployee(id: Int, userId: String): Boolean
}