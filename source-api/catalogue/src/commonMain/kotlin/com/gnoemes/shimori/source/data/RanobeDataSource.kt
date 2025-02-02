package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.titles.MangaOrRanobeWithTrack
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort


interface RanobeDataSource {
    suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<MangaOrRanobeWithTrack>

    suspend fun get(title: Ranobe): RanobeWithTrack
}