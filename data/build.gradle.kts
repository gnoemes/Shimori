plugins {
    id("base-module")
    id("kotlin")
}

dependencies {
    implementation(projects.base.core)
    api(projects.data.core)
    api(projects.data.paging)
}