import com.gnoemes.shimori.convention.ProjectConfig

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
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

                implementation(libs.graphql.apollo)
                implementation(kotlinx.atomicfu)

                implementation(libs.ktor.core)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.contentNegotiation)
                implementation(libs.ktor.okhttp)
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
        packageName.set("${ProjectConfig.APP_PACKAGE}.sources.shikimori")
    }
}
