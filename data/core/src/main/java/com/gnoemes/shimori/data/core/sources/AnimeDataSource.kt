package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort


interface AnimeDataSource {
    suspend fun search(filters: Map<String, String>): List<Anime>
    suspend fun getCalendar(): List<Anime>
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<AnimeWithTrack>
    suspend fun get(title: Anime): AnimeInfo
    suspend fun roles(title: Anime): RolesInfo
}