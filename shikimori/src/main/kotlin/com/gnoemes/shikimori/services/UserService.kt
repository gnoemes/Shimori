package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.club.ClubResponse
import com.gnoemes.shikimori.entities.user.FavoriteListResponse
import com.gnoemes.shikimori.entities.user.UserBriefResponse
import com.gnoemes.shikimori.entities.user.UserDetailsResponse
import com.gnoemes.shikimori.entities.user.UserHistoryResponse

internal interface UserService {
    suspend fun getMeShort(): UserBriefResponse
    suspend fun getShort(id: Long): UserBriefResponse
    suspend fun getFull(id: Long): UserDetailsResponse
    suspend fun getFriends(id: Long): List<UserBriefResponse>
    suspend fun getList(page: Int, limit: Int): List<UserBriefResponse>
    suspend fun getFavorites(id: Long): FavoriteListResponse
    suspend fun getUserHistory(id: Long, page: Int, limit: Int): List<UserHistoryResponse>
    suspend fun getClubs(id: Long): List<ClubResponse>
    suspend fun addToFriends(id: Long)
    suspend fun deleteFriend(id: Long)
    suspend fun ignore(id: Long)
    suspend fun unignore(id: Long)
}