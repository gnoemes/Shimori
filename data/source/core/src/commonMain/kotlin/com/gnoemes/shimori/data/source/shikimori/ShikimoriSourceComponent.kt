package com.gnoemes.shimori.data.source.shikimori

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.source.SourceIds
import com.gnoemes.shimori.source.AuthSource
import com.gnoemes.shimori.sources.shikimori.Shikimori
import com.gnoemes.shimori.sources.shikimori.ShikimoriAuthStore
import com.gnoemes.shimori.sources.shikimori.ShikimoriComponent
import com.gnoemes.shimori.sources.shikimori.ShikimoriId
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.actions.ShikimoriRefreshTokenAction
import com.gnoemes.shimori.sources.shikimori.actions.ShikimoriSignInAction
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriAnimeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriCharacterDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriMangaDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriRanobeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriTrackDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriUserDataSource
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ShikimoriSourceComponent : ShikimoriComponent {

    @IntoSet
    @ApplicationScope
    @Provides
    fun provideShikimoriAuthSource(source: Shikimori): AuthSource = source

    @ApplicationScope
    @Provides
    fun provideShikimoriId(): ShikimoriId = SourceIds.SHIKIMORI

    @ApplicationScope
    @Provides
    fun provideShikimori(
        id: ShikimoriId,
        values: ShikimoriValues,
        anime: ShikimoriAnimeDataSource,
        manga: ShikimoriMangaDataSource,
        ranobe: ShikimoriRanobeDataSource,
        character: ShikimoriCharacterDataSource,
        user: ShikimoriUserDataSource,
        track: ShikimoriTrackDataSource,
        authStore: ShikimoriAuthStore,
        signIn: ShikimoriSignInAction,
        refresh: ShikimoriRefreshTokenAction,
    ): Shikimori =
        Shikimori(
            id,
            values, anime, manga, ranobe, character, user, track,
            authStore,
            signIn, refresh
        )

}