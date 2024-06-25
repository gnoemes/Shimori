plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(kotlinx.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.base)
                api(kotlinx.dateTime)
            }
        }
    }
}