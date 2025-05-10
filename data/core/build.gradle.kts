plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                implementation(projects.sourceBundled.ids)
                implementation(projects.data.source.core)
                implementation(projects.data.syncer)
                implementation(projects.data.lastrequest)
                implementation(projects.data.db.api)
            }
        }
    }
}