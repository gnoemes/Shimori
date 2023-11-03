package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.roles.RolesInfo
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort


interface AnimeDataSource {
    suspend fun search(filters: Map<String, String>): List<Anime>
    suspend fun getCalendar(): List<Anime>
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<AnimeWithTrack>
    suspend fun get(title: Anime): AnimeInfo
    suspend fun roles(title: Anime): RolesInfo
}