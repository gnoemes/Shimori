plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")

    //TODO migrate to Compose Resources
    //Compose resources doesn't support multi module projects right now. Making common:ui:resources basically useless
    //https://github.com/JetBrains/compose-multiplatform/issues/4083
    //Temporary replacing this functionality with lib
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.preferences)

                implementation(projects.common.ui.resources.fonts)
                api(projects.common.ui.resources.strings)
                api(projects.common.ui.resources.icons)

                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.animation)
                implementation(composelibs.material.kolor)
                api(compose.material3)
                api(composelibs.material3.windowsizeclass)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.common.compose"
}