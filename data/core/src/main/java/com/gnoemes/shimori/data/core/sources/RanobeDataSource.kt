package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort


interface RanobeDataSource {
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<RanobeWithTrack>

    suspend fun get(title: Ranobe): RanobeWithTrack

    suspend fun roles(title: Ranobe): RolesInfo
}