plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
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

android {
    namespace = "com.gnoemes.shimori.base"
}