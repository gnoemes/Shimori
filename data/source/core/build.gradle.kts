
plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets{
        val commonMain by getting {
            dependencies {
                api(projects.sources.shikimori.core)

                implementation(projects.core.base)
                implementation(projects.core.preferences)

                implementation(projects.data.db.api)
            }
        }
    }
}

