plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(libs.plugins.graphql.apollo)
    alias(kotlinx.plugins.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.catalogue)
                api(projects.sourceApi.track)
                api(projects.sourceApi.auth)
                api(projects.sources.shikimori.auth)

                implementation(projects.core.base)
                implementation(projects.core.logging.api)

                implementation(libs.graphql.apollo)
                implementation(kotlinx.atomicfu)
                implementation(libs.kotlininject.runtime)

                implementation(libs.ktor.core)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.contentNegotiation)
                implementation(libs.ktor.serialization.json)
            }
        }

        jvmMain {
            dependencies {
                api(libs.ktor.okhttp)
            }
        }
    }
}

apollo {
    service("service") {
        packageName.set("com.gnoemes.shimori.sources.shikimori")
    }
}
