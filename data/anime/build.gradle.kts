plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.data.models)
                implementation(projects.data.db.api)
                implementation(projects.data.source.core)
                implementation(projects.data.lastrequest)
                implementation(projects.data.syncer)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}

