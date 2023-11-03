package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.roles.RolesInfo
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort

interface MangaDataSource {
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<MangaWithTrack>

    suspend fun get(title: Manga): MangaWithTrack

    suspend fun roles(title: Manga): RolesInfo
}