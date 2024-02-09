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
                api(projects.data.models)

                implementation(compose.runtime)
//                implementation(compose.components.resources)

                //TODO migrate to Compose Resources
                api("dev.icerock.moko:resources:0.24.0-alpha-3")
                api("dev.icerock.moko:resources-compose:0.24.0-alpha-3") // for compose multiplatform
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.common.ui.resources.icons"

    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    }
}

multiplatformResources {
    resourcesPackage.set("com.gnoemes.shimori")
    resourcesClassName.set("Res")
}