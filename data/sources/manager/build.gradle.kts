
plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets{
        val commonMain by getting {
            dependencies {
                api(projects.data.sources.shikimori.core)
                implementation(projects.data.db.api)

                implementation(projects.core.base)
                implementation(projects.core.preferences)
                implementation(projects.core.logging.api)
            }
        }
    }
}

