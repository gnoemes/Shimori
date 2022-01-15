package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort

interface AnimeDataSource {

    suspend fun search(): List<Anime>

    suspend fun getCalendar(): List<Anime>

    suspend fun getAnimeWithStatus(user: UserShort, status: RateStatus?): List<Anime>
}