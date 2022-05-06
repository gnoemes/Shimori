plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.base.core)
    commonMainImplementation(projects.data.core)

    androidMainImplementation(projects.base.core)
    androidMainApi(libs.coil.coil)
    androidMainApi(libs.coil.compose)
}