plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(kotlinx.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.core)
                api(libs.multiplatform.oidc)
            }
        }
    }
}