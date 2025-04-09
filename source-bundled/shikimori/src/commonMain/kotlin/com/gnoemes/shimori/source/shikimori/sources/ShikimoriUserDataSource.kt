package com.gnoemes.shimori.source.shikimori.sources

import com.gnoemes.shimori.source.model.SUserProfile
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shimori.source.shikimori.mappers.user.UserResponseMapper
import com.gnoemes.shimori.source.track.UserDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriUserDataSource(
    private val api: ShikimoriApi,
    private val briefMapper: UserBriefMapper,
    private val detailsMapper: UserResponseMapper
) : UserDataSource {

    override suspend fun getMyUser(): SUserProfile {
        return api.user.getMeShort()
            .let { briefMapper.map(it) }
    }

    override suspend fun get(id: SourceIdArgument): SUserProfile {
        return api.user.getFull(id.id)
            .let { detailsMapper.map(it) }
    }

}