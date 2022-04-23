import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("base-module")
    kotlin("android")
}

//for application module
if (extensions.findByType(BaseAppModuleExtension::class.java) != null) {
    configure<BaseAppModuleExtension> {
        compileSdk = com.gnoemes.shimori.Application.compileSdk

        defaultConfig {
            minSdk = com.gnoemes.shimori.Application.minSdk
            targetSdk = com.gnoemes.shimori.Application.targetSdk
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

//for library modules
if (extensions.findByType(LibraryExtension::class.java) != null) {
    configure<LibraryExtension> {
        compileSdk = com.gnoemes.shimori.Application.compileSdk

        defaultConfig {
            minSdk = com.gnoemes.shimori.Application.minSdk
            targetSdk = com.gnoemes.shimori.Application.targetSdk
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}
