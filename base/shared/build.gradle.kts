import com.android.build.api.dsl.LibraryExtension
import com.gnoemes.shimori.initConfigField

plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainApi(projects.base.core)
    commonMainImplementation(libs.multiplatform.logs)

    androidMainApi(libs.kodein.android.core)
    androidMainImplementation(libs.ktor.okhttp)

    jvmMainApi(libs.ktor.okhttp)
}