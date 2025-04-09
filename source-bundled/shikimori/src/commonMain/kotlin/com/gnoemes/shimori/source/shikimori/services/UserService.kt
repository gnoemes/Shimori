package com.gnoemes.shimori.source.shikimori.services

import com.gnoemes.shimori.source.shikimori.models.club.ClubResponse
import com.gnoemes.shimori.source.shikimori.models.user.FavoriteListResponse
import com.gnoemes.shimori.source.shikimori.models.user.UserBriefResponse
import com.gnoemes.shimori.source.shikimori.models.user.UserDetailsResponse

internal interface UserService {
    suspend fun getMeShort(): UserBriefResponse
    suspend fun getShort(id: Long): UserBriefResponse
    suspend fun getFull(id: Long): UserDetailsResponse
    suspend fun getFriends(id: Long): List<UserBriefResponse>
    suspend fun getList(page: Int, limit: Int): List<UserBriefResponse>
    suspend fun getFavorites(id: Long): FavoriteListResponse
    suspend fun getClubs(id: Long): List<ClubResponse>
    suspend fun addToFriends(id: Long)
    suspend fun deleteFriend(id: Long)
    suspend fun ignore(id: Long)
    suspend fun unignore(id: Long)
}