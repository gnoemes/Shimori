plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.base.core)

    androidMainApi(libs.androidx.paging.common)
    jvmMainApi(libs.androidx.paging.common)
}