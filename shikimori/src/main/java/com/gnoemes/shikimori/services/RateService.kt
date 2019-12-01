package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import retrofit2.http.*
import retrofit2.Response

internal interface RateService {

    @GET("/api/users/{id}/anime_rates")
    suspend fun getUserAnimeRates(@Path("id") id: Long,
                                  @Query("page") page: Int,
                                  @Query("limit") limit: Int,
                                  @Query("status") status: String): Response<MutableList<RateResponse>>

    @GET("/api/users/{id}/manga_rates")
    suspend fun getUserMangaRates(@Path("id") id: Long,
                                  @Query("page") page: Int,
                                  @Query("limit") limit: Int,
                                  @Query("status") status: String): Response<MutableList<RateResponse>>

    @GET("/api/v2/user_rates")
    suspend fun getUserRates(@Query("user_id") userId: Long,
                             @Query("target_id") targetId: Long? = null,
                             @Query("target_type") targetType: String? = null,
                             @Query("status") status: String? = null,
                             @Query("page") page: Int? = null,
                             @Query("limit") limit: Int? = null
    ): Response<MutableList<UserRateResponse>>

    @GET("/api/v2/user_rates/{id}")
    suspend fun getRate(@Path("id") id: Long): Response<UserRateResponse>

    @DELETE("/api/v2/user_rates/{id}")
    suspend fun deleteRate(@Path("id") id: Long)

    @POST("/api/v2/user_rates")
    suspend fun createRate(@Body request: UserRateCreateOrUpdateRequest): Response<UserRateResponse>

    @PATCH("/api/v2/user_rates/{id}")
    suspend fun updateRate(@Path("id") id: Long, @Body request: UserRateCreateOrUpdateRequest): Response<UserRateResponse>

    @POST("/api/v2/user_rates/{id}/increment")
    suspend fun increment(@Path("id") id: Long)

}