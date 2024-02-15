package com.gnoemes.shimori.sources.shikimori.models.user

import com.gnoemes.shimori.sources.shikimori.models.common.ShikimoriContentType
import com.gnoemes.shimori.sources.shikimori.models.rates.ShikimoriRateStatus
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StatusResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: ShikimoriRateStatus,
    @SerialName("size") val size: Int,
    @SerialName("type") val type: ShikimoriContentType
)