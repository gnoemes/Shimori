plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(kotlinx.plugins.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.base)
                api(kotlinx.dateTime)
            }
        }
    }
}