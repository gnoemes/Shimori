plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.core)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.core)
                api(projects.data.models)
            }
        }
    }
}