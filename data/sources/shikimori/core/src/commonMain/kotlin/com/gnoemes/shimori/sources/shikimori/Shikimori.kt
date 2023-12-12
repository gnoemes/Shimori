package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.TrackSource
import com.gnoemes.shimori.source.data.AnimeDataSource
import com.gnoemes.shimori.source.data.CharacterDataSource
import com.gnoemes.shimori.source.data.MangaDataSource
import com.gnoemes.shimori.source.data.RanobeDataSource
import com.gnoemes.shimori.source.data.TrackDataSource
import com.gnoemes.shimori.source.data.UserDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriAnimeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriCharacterDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriMangaDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriRanobeDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriTrackDataSource
import com.gnoemes.shimori.sources.shikimori.sources.ShikimoriUserDataSource

//TODO authorization
class Shikimori(
    override val id: Long,
    private val values: ShikimoriValues,
    anime: ShikimoriAnimeDataSource,
    manga: ShikimoriMangaDataSource,
    ranobe: ShikimoriRanobeDataSource,
    character: ShikimoriCharacterDataSource,
    user: ShikimoriUserDataSource,
    track: ShikimoriTrackDataSource,
) : CatalogueSource, TrackSource {

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

}