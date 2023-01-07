package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.core.entities.user.UserShort

interface MangaDataSource {
    suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<MangaWithRate>

    suspend fun get(title: Manga): MangaWithRate

    suspend fun roles(title: Manga): RolesInfo
}