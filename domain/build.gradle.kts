plugins {
    id("base-module")
    id("kotlin")
}

dependencies {
    api(projects.base.core)
    api(projects.data)
    implementation(projects.shikimori)
    implementation(projects.sourceApi)
}