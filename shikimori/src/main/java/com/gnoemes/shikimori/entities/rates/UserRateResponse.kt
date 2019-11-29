package com.gnoemes.shikimori.entities.rates

import com.gnoemes.shikimori.entities.ShikimoriContentType
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

internal data class UserRateResponse(
    @field:SerializedName("id") val id: Long? = null,
    @field:SerializedName("user_id") val userId: Long? = null,
    @field:SerializedName("target_id") val targetId: Long? = null,
    @field:SerializedName("target_type") val targetType: ShikimoriContentType? = null,
    @field:SerializedName("score") val score: Double? = null,
    @field:SerializedName("status") val status: ShikimoriRateStatus? = null,
    @field:SerializedName("rewatches") val rewatches: Int? = null,
    @field:SerializedName("episodes") val episodes: Int? = null,
    @field:SerializedName("volumes") val volumes: Int? = null,
    @field:SerializedName("chapters") val chapters: Int? = null,
    @field:SerializedName("text") val text: String? = null,
    @field:SerializedName("text_html") val textHtml: String? = null,
    @field:SerializedName("created_at") val dateCreated: DateTime? = null,
    @field:SerializedName("updated_at") val dateUpdated: DateTime? = null
)