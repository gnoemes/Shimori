plugins {
    id("base-module")
    id("kotlin")
}

dependencies {
    implementation(projects.base.core)
    api(projects.data.base)
    implementation(projects.data.db)
}