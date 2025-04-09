package com.gnoemes.shimori.source.shikimori.sources

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.source.model.STrack
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.rate.RateMapper
import com.gnoemes.shimori.source.shikimori.models.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shimori.source.track.TrackDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriTrackDataSource(
    private val api: ShikimoriApi,
    private val rateMapper: RateMapper,
) : TrackDataSource {

    override suspend fun getList(id: SourceIdArgument): List<STrack> {
        return api.rate.userRates(id.id)
            .let { rateMapper.forLists().invoke(it) }
    }

    override suspend fun create(track: STrack): STrack {
        return api.rate.create(
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(track))
        ).let { rateMapper.map(it) }
    }

    override suspend fun update(track: STrack): STrack {
        return api.rate.update(
            track.id,
            UserRateCreateOrUpdateRequest(rateMapper.mapInverse(track))
        ).let { rateMapper.map(it) }
    }

    override suspend fun delete(id: SourceIdArgument) {
        return api.rate.delete(id.id)
    }

}