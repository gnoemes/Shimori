package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.model.user.User

interface UserDataSource {

    suspend fun getMyUser(): Result<User>

    suspend fun getUser(userId: Long): Result<User>
}