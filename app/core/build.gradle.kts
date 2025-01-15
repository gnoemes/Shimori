import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.core.addKspDependencyForAllTargets

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
    alias(libs.plugins.buildConfig)
}

@Suppress("PropertyName")
val GithubLink: String by project

buildConfig {
    packageName("${ProjectConfig.APP_PACKAGE}.app")
    buildConfigField(
        type = "String",
        name = "GithubLink",
        value = GithubLink,
    )
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

                api(projects.ui.root)
                api(projects.ui.home)
                api(projects.ui.auth)
                api(projects.ui.settings)
                api(projects.ui.tracks.list)
                api(projects.ui.tracks.menu)
                api(projects.ui.tracks.edit)

                api(libs.graphql.apollo)

                api(libs.slf4j)
            }
        }

        androidMain {
            dependencies {
                implementation(androidx.core)
                api(libs.slf4j.android)
            }
        }

        jvmMain {
            dependencies {
                api(libs.slf4j.desktop)
            }
        }
    }
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)