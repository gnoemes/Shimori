plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(kotlinx.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.auth)

                implementation(projects.core.base)
                implementation(projects.core.preferences)
                implementation(libs.kotlininject.runtime)
                api(libs.multiplatform.oidc)
            }
        }

        androidMain {
            dependencies {
                implementation(projects.core.base)
                implementation(androidx.core)
                implementation(androidx.activity)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.data.source.auth"
}
