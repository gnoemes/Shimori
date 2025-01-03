plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.preferences)
                api(projects.core.logging.api)
                api(projects.data.models)
                api(projects.common.imageloading)

                implementation(projects.common.ui.resources.fonts)
                api(projects.common.ui.resources.strings)
                api(projects.common.ui.resources.icons)

                api(projects.common.ui.screens)
                api(libs.circuit.foundation)

                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.animation)
                implementation(composelibs.material.kolor)
                implementation(composelibs.kmpalette.core)
                implementation(composelibs.kmpalette.extensions.network)

                api(compose.material3)
                api(composelibs.material3.windowsizeclass)
                api(libs.coil.compose)

                api(compose.components.resources)

                implementation(libs.multiplatform.paging.compose)

                implementation(libs.uuid)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.common.compose"
}