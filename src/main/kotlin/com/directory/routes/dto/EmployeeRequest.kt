package com.directory.routes.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRequest(
    val name: String,
    val position: String,
    val phone: String,
    val email: String,
    val department: String
)