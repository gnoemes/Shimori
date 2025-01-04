plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(kotlinx.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.data.source.auth)
                api(projects.sources.shikimori.values)

                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.okhttp)
                implementation(libs.ktor.serialization.json)

                implementation(libs.multiplatform.oidc)
            }
        }

        androidMain {
            dependencies {
                implementation(androidx.core)
                implementation(androidx.browser)
                implementation(androidx.activity)
            }
        }
    }
}