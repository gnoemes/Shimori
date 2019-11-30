package com.gnoemes.shikimori.entities.user

import com.gnoemes.shikimori.entities.common.ShikimoriContentType
import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import com.google.gson.annotations.SerializedName

internal data class StatusResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: ShikimoriRateStatus,
        @field:SerializedName("size") val size: Int,
        @field:SerializedName("type") val type: ShikimoriContentType
)