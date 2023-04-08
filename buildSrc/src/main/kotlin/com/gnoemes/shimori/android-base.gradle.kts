import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("base-module")
    kotlin("android")
}

//for application module
if (extensions.findByType(BaseAppModuleExtension::class) != null) {
    configure<BaseAppModuleExtension> {
        compileSdk = com.gnoemes.shimori.AndroidConfig.compileSdk

        defaultConfig {
            applicationId = com.gnoemes.shimori.AndroidConfig.id
            minSdk = com.gnoemes.shimori.AndroidConfig.minSdk
            targetSdk = com.gnoemes.shimori.AndroidConfig.targetSdk
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

//for library modules
if (extensions.findByType(LibraryExtension::class) != null) {
    configure<LibraryExtension> {
        compileSdk = com.gnoemes.shimori.AndroidConfig.compileSdk

        defaultConfig {
            minSdk = com.gnoemes.shimori.AndroidConfig.minSdk
            targetSdk = com.gnoemes.shimori.AndroidConfig.targetSdk
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

//for test modules
if (extensions.findByType(TestExtension::class) != null) {
    configure<TestExtension> {
        compileSdk = com.gnoemes.shimori.AndroidConfig.compileSdk

        defaultConfig {
            minSdk = com.gnoemes.shimori.AndroidConfig.minSdk
            targetSdk = com.gnoemes.shimori.AndroidConfig.targetSdk
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

