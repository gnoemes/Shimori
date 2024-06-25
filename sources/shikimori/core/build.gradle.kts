import com.gnoemes.shimori.convention.propOrDef

plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(libs.plugins.graphql.apollo3)
    alias(libs.plugins.buildConfig)
    alias(kotlinx.plugins.serialization)
}

buildConfig {
    packageName("com.gnoemes.shimori.sources.shikimori")

    buildConfigField(
        type = "String",
        name = "ShikimoriClientId",
        value = "\"${propOrDef("ShikimoriClientId", "none")}\"",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriClientSecret",
        value = "\"${propOrDef("ShikimoriClientSecret", "none")}\"",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriBaseUrl",
        value = "\"${properties["ShikimoriBaseUrl"]?.toString() ?: ""}\"",
    )
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.sourceApi.catalogue)
                api(projects.sourceApi.track)

                implementation(projects.core.base)
                implementation(projects.core.logging.api)

                implementation(libs.graphql.apollo3)
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

        iosMain {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }

    }
}

apollo {
    service("service") {
        packageName.set("com.gnoemes.shimori.sources.shikimori")
    }
}
