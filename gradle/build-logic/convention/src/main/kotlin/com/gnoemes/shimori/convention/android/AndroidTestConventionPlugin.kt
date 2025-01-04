package com.gnoemes.shimori.convention.android

import com.gnoemes.shimori.convention.core.configureAndroid
import com.gnoemes.shimori.convention.core.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
            }

            configureAndroid()
            configureKotlin()
        }
    }
}