package com.gnoemes.shimori.convention.core

import com.android.build.gradle.BaseExtension
import com.gnoemes.shimori.convention.AndroidConfig
import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroid() {
    android {
        val moduleName = path.split(":").drop(2).joinToString(".")
        namespace = if(moduleName.isNotEmpty()) "${ProjectConfig.APP_PACKAGE}.$moduleName" else ProjectConfig.APP_PACKAGE

        compileSdkVersion(AndroidConfig.COMPILE_SDK)

        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK
            targetSdk = AndroidConfig.TARGET_SDK
        }

        compileOptions {
            // https://developer.android.com/studio/write/java8-support
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    dependencies {
        // https://developer.android.com/studio/write/java8-support
        "coreLibraryDesugaring"(libs.findLibrary("tools.desugarjdklibs").get())
    }
}

private fun Project.android(action: BaseExtension.() -> Unit) = extensions.configure<BaseExtension>(action)
