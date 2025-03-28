package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.source.AuthSource
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.SourceAuthState
import com.gnoemes.shimori.source.TrackSource
import com.gnoemes.shimori.source.data.AnimeDataSource
import com.gnoemes.shimori.source.data.CharacterDataSource
import com.gnoemes.shimori.source.data.MangaDataSource
import com.gnoemes.shimori.source.data.RanobeDataSource
import com.gnoemes.shimori.source.data.TrackDataSource
import com.gnoemes.shimori.source.data.UserDataSource
import com.gnoemes.shimori.sources.shikimori.actions.ShikimoriLogoutAction
import com.gnoemes.shimori.sources.shikimori.actions.ShikimoriRefreshTokenAction
import com.gnoemes.shimori.sources.shikimori.actions.ShikimoriSignInAction
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriAnimeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriCharacterDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriMangaDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriRanobeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriTrackDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriUserDataSource
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = CatalogueSource::class, multibinding = true)
@ContributesBinding(AppScope::class, boundType = TrackSource::class, multibinding = true)
@ContributesBinding(AppScope::class, boundType = AuthSource::class, multibinding = true)
class Shikimori(
    override val id: ShikimoriId,
    private val values: ShikimoriValues,
    anime: ShikimoriAnimeDataSource,
    manga: ShikimoriMangaDataSource,
    ranobe: ShikimoriRanobeDataSource,
    character: ShikimoriCharacterDataSource,
    user: ShikimoriUserDataSource,
    track: ShikimoriTrackDataSource,
    private val authStore: ShikimoriAuthStore,
    private val signInAction: ShikimoriSignInAction,
    private val refreshTokenAction: ShikimoriRefreshTokenAction,
    private val logoutAction: ShikimoriLogoutAction,
) : CatalogueSource, TrackSource, AuthSource {

    companion object {
        const val NAME = "Shikimori"
        internal const val MAX_PAGE_SIZE = 5000

        internal fun String.appendHostIfNeed(values: ShikimoriValues): String {
            return if (this.contains("http")) this else values.url + this
        }
    }

    override val name: String = NAME

    override val animeDataSource: AnimeDataSource = anime
    override val mangaDataSource: MangaDataSource = manga
    override val ranobeDataSource: RanobeDataSource = ranobe
    override val characterDataSource: CharacterDataSource = character
    override val userDataSource: UserDataSource = user
    override val trackDataSource: TrackDataSource = track

    override suspend fun signIn(): SourceAuthState? = signInAction()
    override suspend fun signUp(): SourceAuthState? = signInAction()
    override suspend fun refreshToken(): SourceAuthState? = refreshTokenAction()
    override suspend fun logout()  = logoutAction(Unit)
    override fun getState(): SourceAuthState? = authStore.get()
}