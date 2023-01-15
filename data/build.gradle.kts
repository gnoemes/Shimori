plugins {
    id("base-module")
    id("kotlin")
}

dependencies {
    implementation(projects.base.core)
    implementation(projects.sourceApi)
    api(projects.data.core)
    api(projects.data.paging)
}