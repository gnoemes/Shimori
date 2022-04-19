plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.base.core)

    androidMainImplementation(projects.base.core)
    androidMainApi(libs.coil.coil)
    androidMainApi(libs.coil.compose)
}