package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.user.User


interface UserDataSource {

    suspend fun getMyUser(): User

    suspend fun getUser(id: Long): User
}