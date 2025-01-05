plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                api(projects.common.imageloading)

                implementation(projects.common.ui.resources.fonts)
                api(projects.common.ui.resources.strings)
                api(projects.common.ui.resources.icons)

                api(projects.common.ui.screens)

                api(libs.circuit.foundation)
                api(libs.circuit.codegen.annotations)

                implementation(composelibs.material.kolor)
                implementation(composelibs.kmpalette.core)
                implementation(composelibs.kmpalette.extensions.network)

                api(compose.foundation)
                api(compose.animation)
                api(compose.preview)
                api(compose.material)
                api(compose.material3)
                api(compose.material3AdaptiveNavigationSuite)
                api(composelibs.material3.windowsizeclass)
                api(composelibs.material3.adaptive)
                api(composelibs.material3.adaptive.layout)
                api(composelibs.material3.adaptive.navigation)

                api(libs.coil.compose)
                api(libs.uuid)

                api(compose.components.resources)

                implementation(libs.multiplatform.paging.compose)
            }
        }
    }
}