plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                api(projects.data.eventbus)
                api(projects.data.lists)
                api(projects.data.source.core)
                api(projects.data.anime)
                api(projects.data.manga)
                api(projects.data.ranobe)
                api(projects.data.character)
                api(projects.data.tracks)
                api(projects.data.user)
                api(projects.data.lastrequest)
                api(projects.data.auth)
                api(projects.data.genre)
                api(projects.data.queryable)

                api(libs.multiplatform.paging.common)
                implementation(kotlinx.atomicfu)
            }
        }
    }
}