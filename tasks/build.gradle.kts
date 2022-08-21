plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.domain)

    androidMainApi(libs.androidx.work.runtime)
}