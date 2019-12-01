package com.gnoemes.shimori.data.repositories.user

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.data_base.sources.UserDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    @Shikimori private val source: UserDataSource
) {
    suspend fun getMyUser()  {
        return source.getMyUser()
    }
}