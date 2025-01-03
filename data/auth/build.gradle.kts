plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                implementation(projects.core.base)
                implementation(projects.core.preferences)
                implementation(projects.data.db.api)
                implementation(projects.data.source.core)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}

