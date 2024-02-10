plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(kotlinx.plugins.parcelize)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.circuit.runtime)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.screens"
}