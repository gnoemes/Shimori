package com.gnoemes.shikimori.entities.anime

import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

internal data class CalendarResponse(
    @field:SerializedName("anime") val anime : AnimeResponse,
    @field:SerializedName("next_episode") val nextEpisode : Int,
    @field:SerializedName("next_episode_at") val nextEpisodeDate: OffsetDateTime?,
    @field:SerializedName("duration") val duration : String
)