plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                api(projects.core.settings)
                api(libs.multiplatform.settings.core)
                api(libs.multiplatform.settings.coroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(androidx.core)
                implementation(libs.kotlininject.runtime)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.core.preferences"
}