plugins {
    id("base-module")
    id("kotlin")
}

dependencies {
    api(kotlinx.stdlib)
    api(kotlinx.coroutines.core)
    api(kotlinx.serialization.json)
    api(kotlinx.dateTime)

    api(libs.kodein)
    api(libs.ktor.core)
}