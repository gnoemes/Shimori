plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
}

android {
    namespace = "com.gnoemes.shimori.ui.overlays"
}
kotlin {
    sourceSets {
        val commonMain by getting {
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
