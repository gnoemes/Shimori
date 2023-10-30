plugins {
    id("multiplatform-library")
}

android {
    namespace = "com.gnoemes.shimori.data.paging"
}

dependencies {
    commonMainImplementation(projects.base.core)

    androidMainApi(androidx.paging.common)
    jvmMainApi(androidx.paging.common)
}