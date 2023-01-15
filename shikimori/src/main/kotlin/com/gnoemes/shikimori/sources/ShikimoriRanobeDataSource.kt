package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.ranobe.RanobeDetailsMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeWithRateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shikimori.mappers.roles.RolesMapper
import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.RanobeDataSource

internal class ShikimoriRanobeDataSource(
    private val shikimori: Shikimori,
    private val ratedMapper: RateResponseToRanobeWithRateMapper,
    private val statusMapper: RateStatusMapper,
    private val detailsMapper: RanobeDetailsMapper,
    private val rolesMapper: RolesMapper,
) : RanobeDataSource {

    override suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<RanobeWithTrack> {
        return shikimori.ranobe.getUserRates(user.remoteId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
            .filter { it.entity.ranobeType != null }
    }

    override suspend fun get(title: Ranobe): RanobeWithTrack {
        return shikimori.manga.getDetails(title.id)
            .let { detailsMapper.map(it) }
    }

    override suspend fun roles(title: Ranobe): RolesInfo {
        return shikimori.ranobe.getRoles(title.id)
            .let { rolesMapper.map(it) }
    }
}