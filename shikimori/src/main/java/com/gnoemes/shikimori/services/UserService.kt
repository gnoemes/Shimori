package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.club.ClubResponse
import com.gnoemes.shikimori.entities.user.FavoriteListResponse
import com.gnoemes.shikimori.entities.user.UserBriefResponse
import com.gnoemes.shikimori.entities.user.UserDetailsResponse
import com.gnoemes.shikimori.entities.user.UserHistoryResponse
import retrofit2.http.*
import retrofit2.Response

internal interface UserService {

    @GET("/api/users/whoami")
    suspend fun getMyUserBrief(): Response<UserBriefResponse>

    @GET("/api/users/{id}")
    suspend fun getUserProfile(@Path("id") id: Long): Response<UserDetailsResponse>

    @GET("/api/users/{id}/friends")
    suspend fun getUserFriends(@Path("id") id: Long): Response<MutableList<UserBriefResponse>>

    @GET("/api/users")
    suspend fun getList(@Query("page") page: Int, @Query("limit") limit: Int): Response<MutableList<UserBriefResponse>>

    @GET("/api/users/{id}/info")
    suspend fun getUserBriefInfo(@Path("id") id: Long): Response<UserBriefResponse>

    @GET("/api/users/{id}/favourites")
    suspend fun getUserFavourites(@Path("id") id: Long): Response<FavoriteListResponse>

    @GET("/api/users/{id}/history")
    suspend fun getUserHistory(@Path("id") id: Long, @Query("page") page: Int, @Query("limit") limit: Int): Response<MutableList<UserHistoryResponse>>

    @GET("/api/users/{id}/clubs")
    suspend fun getUserClubs(@Path("id") id: Long): Response<MutableList<ClubResponse>>

    @POST("/api/friends/{id}")
    suspend fun addToFriends(@Path("id") id: Long)

    @DELETE("/api/friends/{id}")
    suspend fun deleteFriend(@Path("id") id: Long)

    @POST(" /api/v2/users/{user_id}/ignore")
    suspend fun ignoreUser(@Path("user_id") id: Long)

    @DELETE(" /api/v2/users/{user_id}/ignore")
    suspend fun unignoreUser(@Path("user_id") id: Long)

}