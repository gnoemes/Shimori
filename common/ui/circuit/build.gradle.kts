plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.ui.compose)
                implementation(projects.common.ui.screens)

                implementation(compose.material3)
                implementation(compose.animation)

                api(libs.circuit.foundation)
                api(libs.circuit.overlay)
            }
        }
    }
}
