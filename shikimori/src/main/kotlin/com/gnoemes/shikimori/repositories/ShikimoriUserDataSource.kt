package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shikimori.mappers.user.UserResponseMapper
import com.gnoemes.shikimori.services.UserService
import com.gnoemes.shimori.base.extensions.bodyOrThrow
import com.gnoemes.shimori.base.extensions.withRetry
import com.gnoemes.shimori.data_base.sources.UserDataSource
import com.gnoemes.shimori.model.user.User
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriUserDataSource @Inject constructor(
    private val service: UserService,
    private val briefMapper: UserBriefMapper,
    private val detailsMapper: UserResponseMapper
) : UserDataSource {

    override suspend fun getMyUser(): User {
        return withRetry {
            service.getMyUserBrief()
                .awaitResponse()
                .let { briefMapper.map(it.bodyOrThrow()) }
        }
    }

    override suspend fun getUser(userId: Long): User {
        return withRetry {
            service.getUserProfile(userId)
                .awaitResponse()
                .let { detailsMapper.map(it.bodyOrThrow()) }
        }
    }
}