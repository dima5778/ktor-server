package com.directory.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import java.io.File
import java.io.FileInputStream

fun Application.configureSecurity() {
    try {
        val serviceAccountStream = if (File("serviceAccountKey.json").exists()) {
            FileInputStream("serviceAccountKey.json")
        } else {
            // Способ 2: Из resources
            this::class.java.classLoader.getResourceAsStream("serviceAccountKey.json")
                ?: throw IllegalStateException("Файл serviceAccountKey.json не найден!")
        }

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
            println("✅ Firebase Admin SDK инициализирован")
        }

        install(Authentication) {
            bearer("firebase-auth") {
                authenticate { tokenCredential ->
                    try {
                        val decodedToken = FirebaseAuth.getInstance()
                            .verifyIdToken(tokenCredential.token)
                        UserIdPrincipal(decodedToken.uid)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
        }
    } catch (e: Exception) {
        println("⚠️  Firebase не удалось инициализировать: ${e.message}")
        e.printStackTrace()
    }
}