plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.settings)
                api(projects.data.models)

                implementation(compose.runtime)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.common.ui.resources.strings"
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.gnoemes.shimori.common.ui.resources.strings"
    generateResClass = always
}
