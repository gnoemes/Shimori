package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort

interface MangaDataSource {
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<MangaWithTrack>

    suspend fun get(title: Manga): MangaWithTrack

    suspend fun roles(title: Manga): RolesInfo
}