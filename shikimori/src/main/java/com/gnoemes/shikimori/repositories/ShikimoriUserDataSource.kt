package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shikimori.mappers.user.UserResponseMapper
import com.gnoemes.shikimori.services.UserService
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.data_base.mappers.toLambda
import com.gnoemes.shimori.data_base.sources.UserDataSource
import com.gnoemes.shimori.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriUserDataSource @Inject constructor(
    private val service: UserService,
    private val briefMapper: UserBriefMapper,
    private val detailsMapper: UserResponseMapper
) : UserDataSource {

    override suspend fun getMyUser(): Result<User> {
        return service.getMyUserBrief()
            .toResult(briefMapper.toLambda())
    }

    override suspend fun getUser(userId: Long): Result<User> {
        return service.getUserProfile(userId)
            .toResult(detailsMapper.toLambda())
    }
}