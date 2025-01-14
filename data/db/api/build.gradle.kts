plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.multiplatform.paging.common)
                implementation(projects.data.models)
            }
        }
    }
}