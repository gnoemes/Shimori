plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets{
        val commonMain by getting {
            dependencies {
                api(projects.sourceApi.core)
                api(projects.data.models)
            }
        }
    }
}