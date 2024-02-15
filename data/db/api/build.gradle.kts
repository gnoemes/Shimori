plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging.api)
                api(projects.data.models)
                api(libs.multiplatform.paging.common)
            }
        }
    }
}