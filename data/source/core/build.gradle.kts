plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceBundled.ids)
                api(projects.data.source.auth)
                api(projects.sourceBundled.shikimori)
                implementation(projects.data.db.api)
                implementation(projects.data.models)
            }
        }
    }
}

