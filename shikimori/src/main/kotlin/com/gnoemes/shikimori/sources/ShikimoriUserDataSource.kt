package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shikimori.mappers.user.UserResponseMapper
import com.gnoemes.shimori.data.core.entities.user.User
import com.gnoemes.shimori.data.core.sources.UserDataSource

internal class ShikimoriUserDataSource(
    private val shikimori: Shikimori,
    private val briefMapper: UserBriefMapper,
    private val detailsMapper: UserResponseMapper,
) : UserDataSource {
    override suspend fun getMyUser(): User {
        return shikimori.user.getMeShort()
            .let { briefMapper.map(it) }
    }

    override suspend fun getUser(id: Long): User {
        return shikimori.user.getFull(id)
            .let { detailsMapper.map(it) }
    }
}