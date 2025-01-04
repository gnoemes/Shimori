import com.gnoemes.shimori.convention.core.addKspDependencyForAllTargets

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
    alias(kotlinx.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.base)
                api(projects.core.logging.impl)
                api(projects.core.settings)
                api(projects.core.preferences)
                api(projects.data.source.core)
                api(projects.data.source.auth)
                api(projects.data.db.sqldelight)
                api(projects.domain)
                api(projects.tasks)

                api(projects.common.imageloading)
                api(projects.common.ui.compose)
                api(projects.common.ui.resources.strings)
                api(projects.common.ui.resources.fonts)
                api(projects.common.ui.resources.icons)

                api(projects.ui.home)
                api(projects.ui.tracks.list)
                api(projects.ui.tracks.menu)
                api(projects.ui.tracks.edit)

                api(libs.graphql.apollo)
            }
        }

        androidMain {
            dependencies {
                implementation(androidx.core)
            }
        }
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)