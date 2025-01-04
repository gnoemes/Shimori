import com.gnoemes.shimori.convention.ProjectConfig

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)

                implementation(compose.runtime)
                implementation(compose.components.resources)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "${ProjectConfig.APP_PACKAGE}.common.ui.resources.icons"
    generateResClass = always
}