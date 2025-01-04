plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)

                implementation(compose.ui)
                implementation(compose.components.resources)
            }
        }
    }
}