plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainImplementation(projects.base.shared)

    androidMainImplementation(libs.androidx.fragment)
}