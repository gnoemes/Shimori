plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.core)
                api(projects.data.models)
            }
        }
    }
}