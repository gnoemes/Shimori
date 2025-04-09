package com.gnoemes.shimori.source.shikimori.models.user

import com.gnoemes.shimori.source.shikimori.models.common.ShikimoriContentType
import com.gnoemes.shimori.source.shikimori.models.rates.ShikimoriRateStatus
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StatusResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: ShikimoriRateStatus,
    @SerialName("size") val size: Int,
    @SerialName("type") val type: ShikimoriContentType
)