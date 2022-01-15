package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.model.user.User

interface UserDataSource {

    suspend fun getMyUser(): User

    suspend fun getUser(userId: Long): User
}