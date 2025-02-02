package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.titles.MangaOrRanobeWithTrack
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort

interface MangaDataSource {
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<MangaOrRanobeWithTrack>

    suspend fun get(title: Manga): MangaWithTrack

}