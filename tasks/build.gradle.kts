plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.domain)
                implementation(libs.kotlininject.runtime)
            }
        }

        androidMain {
            dependencies {
                api(androidx.work.runtime)
            }
        }
    }
}


android {
    namespace = "com.gnoemes.shimori.tasks"
}