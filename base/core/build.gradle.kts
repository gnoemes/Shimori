plugins {
    id("kotlin")
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.kotlin.coroutines.core)
    api(libs.kotlin.serialization.json)

    api(libs.kodein)
    api(libs.ktor.core)

    api(libs.kotlin.dateTime)
}