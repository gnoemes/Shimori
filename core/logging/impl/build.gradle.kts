plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.base)
                api(projects.core.logging.api)
                implementation(libs.multiplatform.logs)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.google.crashlytics)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.logging.impl"
}