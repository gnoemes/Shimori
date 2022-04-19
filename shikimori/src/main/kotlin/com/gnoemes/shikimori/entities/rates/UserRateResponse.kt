package com.gnoemes.shikimori.entities.rates

import com.gnoemes.shikimori.entities.common.ShikimoriContentType
import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class UserRateResponse(
    @SerialName("id") val id: Long,
    @SerialName("user_id") val userId: Long? = null,
    @SerialName("target_id") val targetId: Long,
    @SerialName("target_type") val targetType: ShikimoriContentType,
    @SerialName("score") val score: Double? = null,
    @SerialName("status") val status: ShikimoriRateStatus? = null,
    @SerialName("rewatches") val rewatches: Int? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("volumes") val volumes: Int? = null,
    @SerialName("chapters") val chapters: Int? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("text_html") val textHtml: String? = null,
    @SerialName("created_at") val dateCreated: DateTimePeriod? = null,
    @SerialName("updated_at") val dateUpdated: DateTimePeriod? = null
)