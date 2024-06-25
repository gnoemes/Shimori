plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging.api)

                implementation(projects.data.models)

                api(libs.coil.core)
                api(libs.coil.network)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.common.imageloading"
}