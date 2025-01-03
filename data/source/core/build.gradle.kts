plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sources.shikimori.core)
                api(projects.data.source.auth)

                implementation(projects.core.base)
                implementation(projects.core.preferences)

                implementation(projects.data.db.api)
            }
        }
    }
}

