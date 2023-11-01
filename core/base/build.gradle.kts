plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlinx.coroutines.core)
                api(kotlinx.serialization.json)
                api(kotlinx.dateTime)

                api(libs.ktor.core)
                api(libs.kotlininject.runtime)
            }
        }

        val jvmMain by getting
    }
}