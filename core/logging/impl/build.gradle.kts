plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.base)
                api(projects.core.logging.api)
                implementation(libs.multiplatform.logs)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.google.crashlytics)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.logging.impl"
}