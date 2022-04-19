plugins {
    id("base-module")
    id("kotlin")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(projects.base.core)
    implementation(projects.data.base)

    implementation(libs.ktor.auth)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.serialization.json)

    implementation(libs.kotlin.dateTime)
    implementation(libs.ktor.serialization.json)
}