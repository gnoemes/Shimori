plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                implementation(projects.data.db.api)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}

