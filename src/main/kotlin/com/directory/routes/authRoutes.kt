package com.directory.routes

import com.directory.routes.dto.AuthRequest
import com.directory.routes.dto.AuthResponse
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/auth") {
        get("/ping") {
            call.respond(HttpStatusCode.OK, "Auth endpoint работает")
        }
    }
}