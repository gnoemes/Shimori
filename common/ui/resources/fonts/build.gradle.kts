plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.data.models)

                implementation(compose.ui)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.common.ui.resources.fonts"

    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    }
}