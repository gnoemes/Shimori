package com.gnoemes.shimori.sources.shikimori.sources

import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.source.data.UserDataSource
import com.gnoemes.shimori.sources.shikimori.ShikimoriApi
import com.gnoemes.shimori.sources.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shimori.sources.shikimori.mappers.user.UserResponseMapper
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriUserDataSource(
    private val api: ShikimoriApi,
    private val briefMapper: UserBriefMapper,
    private val detailsMapper : UserResponseMapper
) : UserDataSource {

    override suspend fun getMyUser(): User {
        return api.user.getMeShort()
            .let { briefMapper.map(it) }
    }

    override suspend fun getUser(id: Long): User {
        return api.user.getFull(id)
            .let { detailsMapper.map(it) }
    }
}