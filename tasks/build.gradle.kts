plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain)
            }
        }

        androidMain {
            dependencies {
                api(androidx.work.runtime)
            }
        }
    }
}