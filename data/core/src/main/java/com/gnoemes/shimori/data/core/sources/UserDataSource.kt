package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.user.User


interface UserDataSource {

    suspend fun getMyUser(): User

    suspend fun getUser(id: Long): User
}