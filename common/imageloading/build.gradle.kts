plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.data.models)

                api(libs.coil.core)
                api(libs.coil.network)
            }
        }
    }
}