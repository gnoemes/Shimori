package com.gnoemes.shikimori.entities.user

import com.gnoemes.shikimori.entities.common.ShikimoriContentType
import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class StatusResponse(
        @SerialName("id") val id: Long,
        @SerialName("name") val name: ShikimoriRateStatus,
        @SerialName("size") val size: Int,
        @SerialName("type") val type: ShikimoriContentType
)