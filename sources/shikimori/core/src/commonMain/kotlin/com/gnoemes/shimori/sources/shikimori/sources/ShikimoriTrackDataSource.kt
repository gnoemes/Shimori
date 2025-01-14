package com.gnoemes.shimori.sources.shikimori.sources

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.data.TrackDataSource
import com.gnoemes.shimori.sources.shikimori.ShikimoriApi
import com.gnoemes.shimori.sources.shikimori.mappers.rate.RateMapper
import com.gnoemes.shimori.sources.shikimori.models.rates.UserRateCreateOrUpdateRequest
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriTrackDataSource(
    private val api: ShikimoriApi,
    private val rateMapper: RateMapper,
) : TrackDataSource {
    override suspend fun getList(user: UserShort): List<Track> {
        return api.rate.userRates(user.remoteId)
            .let { rateMapper.forLists().invoke(it) }
    }

    override suspend fun create(track: Track): Track {
        return api.rate.create(
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(track))
        ).let { rateMapper.map(it) }
    }

    override suspend fun update(track: Track): Track {
        return api.rate.update(
            track.id,
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(track))
        ).let { rateMapper.map(it) }
    }

    override suspend fun delete(id: Long) {
        return api.rate.delete(id)
    }
}