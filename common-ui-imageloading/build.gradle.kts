plugins {
    id("multiplatform-library")
}

android {
    namespace = "com.gnoemes.shimori.common_ui_imageloading"
}

dependencies {
    commonMainImplementation(projects.base.core)
    commonMainImplementation(projects.data.core)

    androidMainImplementation(projects.base.core)
    androidMainApi(libs.coil.coil)
    androidMainApi(libs.coil.compose)
}