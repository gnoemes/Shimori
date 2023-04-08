plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.domain)

    androidMainApi(androidx.work.runtime)
}