plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
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

android {
    namespace = "com.gnoemes.shimori.common.ui.resources.fonts"
}