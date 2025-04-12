package com.gnoemes.shimori.data

import app.cash.sqldelight.db.SqlDriver
import com.gnoemes.shimori.data.adapters.AnimeAdapter
import com.gnoemes.shimori.data.adapters.AnimeVideoAdapter
import com.gnoemes.shimori.data.adapters.CharacterRoleAdapter
import com.gnoemes.shimori.data.adapters.GenreAdapter
import com.gnoemes.shimori.data.adapters.GenreRelationAdapter
import com.gnoemes.shimori.data.adapters.LastRequestAdapter
import com.gnoemes.shimori.data.adapters.ListSortAdapter
import com.gnoemes.shimori.data.adapters.MangaAdapter
import com.gnoemes.shimori.data.adapters.PinnedAdapter
import com.gnoemes.shimori.data.adapters.RanobeAdapter
import com.gnoemes.shimori.data.adapters.SourceIdsSyncAdapter
import com.gnoemes.shimori.data.adapters.TrackAdapter
import com.gnoemes.shimori.data.adapters.TrackToSyncAdapter
import com.gnoemes.shimori.data.adapters.UserAdapter
import me.tatarka.inject.annotations.Inject

@Inject
class DatabaseFactory(
    private val driver: SqlDriver
) {

    fun build(): ShimoriDB = ShimoriDB(
        driver = driver,
        animeAdapter = AnimeAdapter,
        anime_videoAdapter = AnimeVideoAdapter,
        character_roleAdapter = CharacterRoleAdapter,
        last_requestAdapter = LastRequestAdapter,
        list_sortAdapter = ListSortAdapter,
        mangaAdapter = MangaAdapter,
        ranobeAdapter = RanobeAdapter,
        source_ids_syncAdapter = SourceIdsSyncAdapter,
        trackAdapter = TrackAdapter,
        track_to_syncAdapter = TrackToSyncAdapter,
        genre_relationAdapter = GenreRelationAdapter,
        genreAdapter = GenreAdapter,
        userAdapter = UserAdapter,
        pinnedAdapter = PinnedAdapter
    )

}