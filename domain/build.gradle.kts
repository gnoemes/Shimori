plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)

                api(projects.data.models)
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

                api(libs.multiplatform.paging.common)
                implementation(kotlinx.atomicfu)
                implementation(libs.kotlininject.runtime)
            }
        }
    }
}