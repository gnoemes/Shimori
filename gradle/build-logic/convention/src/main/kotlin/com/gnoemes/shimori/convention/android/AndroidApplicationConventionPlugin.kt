package com.gnoemes.shimori.convention.android

import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.core.configureAndroid
import com.gnoemes.shimori.convention.core.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                if (!hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    apply("org.jetbrains.kotlin.android")
                }
                apply("${ProjectConfig.APP_PACKAGE}.compose")
                apply("org.gradle.android.cache-fix")
            }

            configureAndroid()
            configureKotlin()
        }
    }
}