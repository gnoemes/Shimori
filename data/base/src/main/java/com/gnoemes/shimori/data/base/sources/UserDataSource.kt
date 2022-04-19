package com.gnoemes.shimori.data.base.sources

import com.gnoemes.shimori.data.base.entities.user.User


interface UserDataSource {

    suspend fun getMyUser(): User

    suspend fun getUser(id: Long): User
}