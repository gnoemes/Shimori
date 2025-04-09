package com.gnoemes.shimori.source.shikimori

import com.gnoemes.shimori.source.auth.AuthSource
import com.gnoemes.shimori.source.catalogue.AnimeDataSource
import com.gnoemes.shimori.source.catalogue.CatalogueSource
import com.gnoemes.shimori.source.catalogue.CharacterDataSource
import com.gnoemes.shimori.source.catalogue.MangaDataSource
import com.gnoemes.shimori.source.catalogue.PersonDataSource
import com.gnoemes.shimori.source.catalogue.RanobeDataSource
import com.gnoemes.shimori.source.model.SourceAuthState
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.actions.ShikimoriLogoutAction
import com.gnoemes.shimori.source.shikimori.actions.ShikimoriRefreshTokenAction
import com.gnoemes.shimori.source.shikimori.actions.ShikimoriSignInAction
import com.gnoemes.shimori.source.shikimori.auth.ShikimoriAuthStore
import com.gnoemes.shimori.source.shikimori.sources.ShikimoriAnimeDataSource
import com.gnoemes.shimori.source.shikimori.sources.ShikimoriCharacterDataSource
import com.gnoemes.shimori.source.shikimori.sources.ShikimoriMangaDataSource
import com.gnoemes.shimori.source.shikimori.sources.ShikimoriRanobeDataSource
import com.gnoemes.shimori.source.shikimori.sources.ShikimoriTrackDataSource
import com.gnoemes.shimori.source.shikimori.sources.ShikimoriUserDataSource
import com.gnoemes.shimori.source.track.TrackDataSource
import com.gnoemes.shimori.source.track.TrackSource
import com.gnoemes.shimori.source.track.UserDataSource
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
    override val values: ShikimoriValues,
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

    override val malIdsSupport: List<SourceDataType> = listOf(
        SourceDataType.Anime, SourceDataType.Manga, SourceDataType.Ranobe,
    )

    override val availableData: List<SourceDataType> = listOf(
        SourceDataType.Anime, SourceDataType.Manga, SourceDataType.Ranobe,
        SourceDataType.Track, SourceDataType.Character, SourceDataType.Person,
    )

    override val animeDataSource: AnimeDataSource = anime
    override val mangaDataSource: MangaDataSource = manga
    override val ranobeDataSource: RanobeDataSource = ranobe
    override val characterDataSource: CharacterDataSource = character
    override val personDataSource: PersonDataSource get() = TODO("Not yet implemented")
    override val userDataSource: UserDataSource = user
    override val trackDataSource: TrackDataSource = track

    override suspend fun signIn(): SourceAuthState? = signInAction()
    override suspend fun signUp(): SourceAuthState? = signInAction()
    override suspend fun refreshToken(): SourceAuthState? = refreshTokenAction()
    override suspend fun logout() = logoutAction(Unit)
    override fun getState(): SourceAuthState? = authStore.get()
}