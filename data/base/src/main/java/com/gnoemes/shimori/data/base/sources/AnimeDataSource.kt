package com.gnoemes.shimori.data.base.sources

import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.entities.user.UserShort


interface AnimeDataSource {

    suspend fun search(filters : Map<String, String>): List<Anime>

    suspend fun getCalendar(): List<Anime>

    suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<Anime>
}