package com.gnoemes.shimori.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.gradle.android.cache-fix")
            }

            configureAndroid()
        }
    }
}