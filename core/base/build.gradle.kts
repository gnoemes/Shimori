plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.logging.api)
                api(kotlinx.coroutines.core)
                api(kotlinx.serialization.json)
                api(kotlinx.dateTime)

                api(libs.ktor.core)
                api(libs.kotlininject.runtime)
            }
        }

        androidMain {
            dependencies {
                api(androidx.core)
            }
        }
    }
}