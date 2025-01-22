import com.gnoemes.shimori.convention.ProjectConfig

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.settings)
                api(projects.data.models)
                api(projects.sourceApi.core)

                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.components.resources)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "${ProjectConfig.APP_PACKAGE}.common.ui.resources.strings"
    generateResClass = always
}
