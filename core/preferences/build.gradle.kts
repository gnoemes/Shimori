plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                api(projects.core.settings)
                api(libs.multiplatform.settings.core)
                api(libs.multiplatform.settings.coroutines)
            }
        }

        androidMain {
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