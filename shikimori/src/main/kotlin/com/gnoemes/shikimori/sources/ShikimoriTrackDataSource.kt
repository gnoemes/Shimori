package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.TrackDataSource

internal class ShikimoriTrackDataSource(
    private val shikimori: Shikimori,
    private val rateMapper: RateMapper,
) : TrackDataSource {
    override suspend fun getList(user: UserShort): List<Track> {
        return shikimori.rate.userRates(user.remoteId)
            .let { rateMapper.forLists().invoke(it) }
    }

    override suspend fun create(track: Track): Track {
        return shikimori.rate.create(
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(track))
        ).let { rateMapper.map(it) }
    }

    override suspend fun update(track: Track): Track {
        return shikimori.rate.update(
            track.id,
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(track))
        ).let { rateMapper.map(it) }
    }

    override suspend fun delete(id: Long) {
        return shikimori.rate.delete(id)
    }
}