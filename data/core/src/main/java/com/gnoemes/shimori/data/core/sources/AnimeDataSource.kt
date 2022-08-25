package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.core.entities.user.UserShort


interface AnimeDataSource {

    suspend fun search(filters : Map<String, String>): List<Anime>

    suspend fun getCalendar(): List<Anime>

    suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<AnimeWithRate>

    suspend fun get(title: Anime) : AnimeWithRate
}