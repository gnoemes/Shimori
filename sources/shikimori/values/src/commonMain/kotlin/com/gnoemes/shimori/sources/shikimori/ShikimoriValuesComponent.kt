package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.app.SourceValues
import me.tatarka.inject.annotations.Provides


interface ShikimoriValuesComponent  {

    @ApplicationScope
    @Provides
    fun provideShikimoriValues(
        info: ApplicationInfo
    ): ShikimoriValues {
        val baseUrl = BuildConfig.ShikimoriBaseUrl

        return SourceValues(
            url = baseUrl,
            clientId = BuildConfig.ShikimoriClientId,
            secretKey = BuildConfig.ShikimoriClientSecret,
            oauthRedirect = "shimori://oauth",
            signInUrl = "$baseUrl/users/sign_in",
            signUpUrl = "$baseUrl/users/sign_up",
            oAuthUrl = "$baseUrl/oauth/authorize",
            tokenUrl = "$baseUrl/oauth/token",
            userAgent = info.name,
        )
    }
}

typealias ShikimoriValues = SourceValues
typealias ShikimoriId = Long