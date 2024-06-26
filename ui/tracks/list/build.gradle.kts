plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
}

android {
    namespace = "com.gnoemes.shimori.tracks.list"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.common.ui.compose)
                implementation(projects.common.ui.screens)
                implementation(projects.common.ui.circuitOverlay)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.retained)
                implementation(libs.circuit.gestureNavigation)
                implementation(libs.circuit.overlay)

                implementation(compose.foundation)
            }
        }

        androidMain {
            dependencies {
                implementation(androidx.activity.compose)
            }
        }
    }
}
