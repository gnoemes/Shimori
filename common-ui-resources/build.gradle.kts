plugins {
    id("multiplatform-library")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    android()
    jvm()

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.moko.resources.compose)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.moko.resources.compose)
            }
        }
    }

    multiplatformResources {
        multiplatformResourcesPackage = com.gnoemes.shimori.Application.id
    }

    dependencies {
        commonMainApi(libs.moko.resources.resources)
        commonTestImplementation(libs.moko.resources.test)
    }
}





