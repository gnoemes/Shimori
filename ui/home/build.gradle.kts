plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")

    //TODO migrate to Compose Resources
    //Compose resources doesn't support multi module projects right now. Making common:ui:resources basically useless
    //https://github.com/JetBrains/compose-multiplatform/issues/4083
    //Temporary replacing this functionality with lib
    id("dev.icerock.mobile.multiplatform-resources")
}

android {
    namespace = "com.gnoemes.shimori.home"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.common.ui.compose)
                implementation(projects.common.ui.screens)
                implementation(projects.common.ui.circuitOverlay)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.foundation)
                implementation(libs.circuit.retained)
                implementation(libs.circuit.gestureNavigation)
                implementation(libs.circuit.overlay)

                implementation(compose.foundation)

                //TODO migrate to Compose Resources
                api("dev.icerock.moko:resources:0.24.0-alpha-3")
                api("dev.icerock.moko:resources-compose:0.24.0-alpha-3") // for compose multiplatform
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(androidx.activity.compose)
            }
        }
    }
}
