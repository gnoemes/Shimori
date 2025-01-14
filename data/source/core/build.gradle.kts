plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sources.ids)
                api(projects.sources.shikimori.core)
                api(projects.data.source.auth)
                implementation(projects.data.db.api)
            }
        }
    }
}

