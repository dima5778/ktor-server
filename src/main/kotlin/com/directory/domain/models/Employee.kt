package com.directory.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Int? = null,
    val userId: String = "",
    val name: String,
    val position: String,
    val phone: String,
    val email: String,
    val department: String
)