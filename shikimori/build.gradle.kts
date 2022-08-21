plugins {
    id("base-module")
    id("kotlin")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(projects.base.core)
    implementation(projects.data.core)

    implementation(libs.ktor.auth)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.serialization.json)
}