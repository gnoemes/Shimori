plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
            }
        }
    }
}