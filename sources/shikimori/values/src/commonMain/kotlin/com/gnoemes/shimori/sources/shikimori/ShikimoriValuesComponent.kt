package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.entities.isDesktop
import com.gnoemes.shimori.data.app.SourceValues
import com.gnoemes.shimori.sources.SourceIds
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@ContributesTo(AppScope::class)
interface ShikimoriValuesComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideId() : ShikimoriId = SourceIds.SHIKIMORI

    @SingleIn(AppScope::class)
    @Provides
    fun provideShikimoriValues(
        info: ApplicationInfo
    ): ShikimoriValues {
        return SourceValues(
            url = BuildConfig.ShikimoriBaseUrl,
            clientId =
            if (info.platform.isDesktop) BuildConfig.ShikimoriClientIdDesktop
            else BuildConfig.ShikimoriClientId,
            secretKey =
            if (info.platform.isDesktop) BuildConfig.ShikimoriClientSecretDesktop
            else BuildConfig.ShikimoriClientSecret,
            oauthRedirect =
            if (info.platform.isDesktop) BuildConfig.ShikimoriRedirectDesktop
            else BuildConfig.ShikimoriRedirect,
            signInUrl = BuildConfig.ShikimoriSignInUrl,
            signUpUrl = BuildConfig.ShikimoriSignUpUrl,
            oAuthUrl = BuildConfig.ShikimoriAuthorizeUrl,
            tokenUrl = BuildConfig.ShikimoriTokenUrl,
            userAgent =
            if (info.platform.isDesktop) "${info.name} (Desktop)"
            else info.name,
        )
    }
}

typealias ShikimoriValues = SourceValues
typealias ShikimoriId = Long