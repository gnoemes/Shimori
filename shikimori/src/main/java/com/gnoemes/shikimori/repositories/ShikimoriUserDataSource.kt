package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.services.UserService
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.data_base.sources.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriUserDataSource @Inject constructor(
    private val service: UserService
) : UserDataSource {

    override suspend fun getMyUser() {
       val response =  service.getMyUserBrief()
           .toResult()
    }
}