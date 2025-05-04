plugins {
    alias(libs.plugins.shimori.compose.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.multiplatform.paging.compose)
            }
        }
    }
}