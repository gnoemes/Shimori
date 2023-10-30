plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.gnoemes.shimori.base.shared"
}

dependencies {
    commonMainApi(projects.base.core)
    commonMainImplementation(libs.multiplatform.logs)

    androidMainApi(libs.kodein.android.core)
    androidMainImplementation(libs.ktor.okhttp)

    jvmMainApi(libs.ktor.okhttp)
}