plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
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

