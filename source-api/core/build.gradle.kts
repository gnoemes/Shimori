plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.core)
    alias(kotlinx.plugins.serialization)
}



kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlinx.serialization.json)
                api(kotlinx.dateTime)
            }
        }
    }
}