plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(kotlinx.plugins.serialization)
}

android {
    namespace = "com.gnoemes.shimori.sources.shikimori"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging.api)

                implementation(projects.data.source.auth)
                api(projects.sources.shikimori.values)

                implementation(libs.kotlininject.runtime)

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