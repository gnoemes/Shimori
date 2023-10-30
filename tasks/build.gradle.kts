plugins {
    id("multiplatform-library")
}

android {
    namespace = "com.gnoemes.shimori.tasks"
}

dependencies {
    commonMainImplementation(projects.domain)

    androidMainApi(androidx.work.runtime)
}