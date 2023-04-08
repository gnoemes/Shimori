plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.base.core)

    androidMainApi(androidx.paging.common)
    jvmMainApi(androidx.paging.common)
}