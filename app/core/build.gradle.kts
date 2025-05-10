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

                api(projects.data.db.sqldelight)
                api(projects.data.lists)
                api(projects.data.source.core)
                api(projects.data.anime)
                api(projects.data.manga)
                api(projects.data.ranobe)
                api(projects.data.character)
                api(projects.data.person)
                api(projects.data.tracks)
                api(projects.data.user)
                api(projects.data.lastrequest)
                api(projects.data.auth)
                api(projects.data.genre)
                api(projects.data.studio)
                api(projects.data.queryable)

                api(projects.sourceBundled.shikimori)

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

                api(projects.ui.title.details)
                api(projects.ui.title.characters)
                api(projects.ui.title.trailers)
                api(projects.ui.title.related)

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