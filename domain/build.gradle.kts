plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.models)
                api(projects.data.eventbus)

                implementation(projects.data.lists)
                implementation(projects.data.source.core)
                implementation(projects.data.anime)
                implementation(projects.data.manga)
                implementation(projects.data.ranobe)
                implementation(projects.data.character)
                implementation(projects.data.person)
                implementation(projects.data.tracks)
                implementation(projects.data.user)
                implementation(projects.data.lastrequest)
                implementation(projects.data.auth)
                implementation(projects.data.genre)
                implementation(projects.data.studio)
                implementation(projects.data.queryable)

                api(libs.multiplatform.paging.common)
                implementation(kotlinx.atomicfu)
            }
        }
    }
}