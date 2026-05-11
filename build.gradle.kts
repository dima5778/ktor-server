plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"

    id("io.ktor.plugin") version "3.0.3"
    application
}

group = "com.directory"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("com.directory.ApplicationKt")
}

dependencies {
    // Ktor Server из вашего каталога libs
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.resources)

    // Database & Utils
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.hikari)
    implementation(libs.postgresql)
    implementation(libs.h2database.h2)
    implementation(libs.logback.classic)
    implementation("com.google.guava:guava:33.4.0-jre")
    // Firebase Admin (если нужен)
    implementation("com.google.firebase:firebase-admin:9.3.0") {
        exclude(group = "com.google.guava", module = "guava")
    }
    // Tests
    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
}