plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.core)
    alias(libs.plugins.shimori.ksp.anvil)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging.api)
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
