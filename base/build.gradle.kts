plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlin.stdlib)
                api(libs.kotlin.coroutines.core)
                api(libs.kotlin.serialization.json)
                api(libs.kodein)
                api(libs.ktor.core)

                implementation(libs.kotlin.dateTime)
            }
        }

    }
}