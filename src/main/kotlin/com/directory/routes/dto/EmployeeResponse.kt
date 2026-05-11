package com.directory.routes.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeResponse(
    val id: Int,
    val name: String,
    val position: String,
    val phone: String,
    val email: String,
    val department: String
)