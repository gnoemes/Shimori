plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.core)
                api(projects.sourceBundled.ids)
                api(projects.sourceBundled.shikimori)

                api(projects.data.source.auth)
                implementation(projects.data.db.api)
                implementation(projects.data.models)
            }
        }
    }
}

