plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                implementation(projects.data.db.api)
            }
        }
    }
}