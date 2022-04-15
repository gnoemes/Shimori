plugins {
    id("kotlin")
    kotlin("plugin.serialization")
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.kotlin.coroutines.core)
    api(libs.kotlin.serialization.json)
    api(libs.kodein)
    api(libs.ktor.core)
}