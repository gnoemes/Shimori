package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import retrofit2.Call
import retrofit2.http.*

internal interface RateService {

    @GET("/api/v2/user_rates")
    fun getUserRates(@Query("user_id") userId: Long,
                             @Query("target_id") targetId: Long? = null,
                             @Query("target_type") targetType: String? = null,
                             @Query("status") status: String? = null,
                             @Query("page") page: Int? = null,
                             @Query("limit") limit: Int? = null
    ): Call<MutableList<UserRateResponse>>

    @GET("/api/v2/user_rates/{id}")
    fun getRate(@Path("id") id: Long): Call<UserRateResponse>

    @DELETE("/api/v2/user_rates/{id}")
    fun deleteRate(@Path("id") id: Long): Call<Unit>

    @POST("/api/v2/user_rates")
    fun createRate(@Body request: UserRateCreateOrUpdateRequest): Call<UserRateResponse>

    @PATCH("/api/v2/user_rates/{id}")
    fun updateRate(@Path("id") id: Long, @Body request: UserRateCreateOrUpdateRequest): Call<UserRateResponse>

    @POST("/api/v2/user_rates/{id}/increment")
    fun increment(@Path("id") id: Long)

}