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
    //route("/auth") {
        //post("/login") {
            //val request = call.receive<AuthRequest>()

            //try {
                // Этот способ работает через Firebase REST API (нужен API Key)
                // Пока сделаем заглушку + объясню как правильно

                // Вариант для курсовой (простой)
               // val userRecord = FirebaseAuth.getInstance().getUserByEmail(request.email)

                // Создаём custom token
               // val customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.uid)

               // call.respond(HttpStatusCode.OK, AuthResponse(
                   // token = customToken,
                    //userId = userRecord.uid
               // ))

           // } catch (e: Exception) {
                //call.respond(HttpStatusCode.Unauthorized, "Неверный email или пароль")
            //}
       // }

        // Регистрация
       // post("/register") {
           // val request = call.receive<AuthRequest>()
            // Здесь можно добавить создание пользователя
           // call.respond(HttpStatusCode.NotImplemented, "Регистрация в разработке")
        //}
   // }
}