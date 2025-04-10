@file:Suppress("PropertyName")

import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.propOrDef

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.graphql.apollo)
    alias(kotlinx.plugins.serialization)
    alias(libs.plugins.buildConfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.sourceApi.core)
                implementation(projects.sourceBundled.ids)

                implementation(libs.graphql.apollo)
                implementation(kotlinx.atomicfu)

                implementation(libs.ktor.core)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.contentNegotiation)
                implementation(libs.ktor.okhttp)
                implementation(libs.ktor.serialization.json)

                implementation(libs.multiplatform.oidc)
            }
        }

        androidMain {
            dependencies {
                implementation(androidx.core)
                implementation(androidx.browser)
                implementation(androidx.activity)
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
        packageName.set("${ProjectConfig.APP_PACKAGE}.source.shikimori")
    }
}


val baseUrl: String by project
val authorize = "$baseUrl/oauth/authorize"
val token = "$baseUrl/oauth/token"
val signIn = "$baseUrl/users/sign_in"
val signUp = "$baseUrl/users/sign_up"

val ShikimoriClientId: String? by project
val ShikimoriClientSecret: String? by project
val ShikimoriRedirectUrl: String? by project

val ShikimoriClientIdDesktop: String? by project
val ShikimoriClientSecretDesktop: String? by project
val ShikimoriRedirectUrlDesktop: String by project

buildConfig {
    packageName("${ProjectConfig.APP_PACKAGE}.source.shikimori")

    buildConfigField(
        type = "String",
        name = "ShikimoriBaseUrl",
        value = baseUrl,
    )

    buildConfigField(
        type = "String",
        name = "ShikimoriAuthorizeUrl",
        value = authorize,
    )

    buildConfigField(
        type = "String",
        name = "ShikimoriTokenUrl",
        value = token,
    )

    buildConfigField(
        type = "String",
        name = "ShikimoriSignInUrl",
        value = signIn,
    )

    buildConfigField(
        type = "String",
        name = "ShikimoriSignUpUrl",
        value = signUp,
    )

    buildConfigField(
        type = "String",
        name = "ShikimoriClientId",
        value = ShikimoriClientId ?: "${propOrDef("ShikimoriClientId", "none")}",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriClientSecret",
        value = ShikimoriClientSecret ?: "${propOrDef("ShikimoriClientSecret", "none")}",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriRedirect",
        value = ShikimoriRedirectUrl ?: "${propOrDef("ShikimoriRedirectUrl", "none")}",
    )

    buildConfigField(
        type = "String",
        name = "ShikimoriClientIdDesktop",
        value = ShikimoriClientIdDesktop ?: "${propOrDef("ShikimoriClientIdDesktop", "none")}",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriClientSecretDesktop",
        value = ShikimoriClientSecretDesktop ?: "${
            propOrDef(
                "ShikimoriClientSecretDesktop",
                "none"
            )
        }",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriRedirectDesktop",
        value = ShikimoriRedirectUrlDesktop,
    )
}