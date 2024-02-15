plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)

                api(projects.data.lists)
                implementation(projects.data.source.core)
                implementation(projects.data.anime)
                implementation(projects.data.manga)
                implementation(projects.data.ranobe)
                implementation(projects.data.tracks)
                implementation(projects.data.user)
            }
        }
    }
}