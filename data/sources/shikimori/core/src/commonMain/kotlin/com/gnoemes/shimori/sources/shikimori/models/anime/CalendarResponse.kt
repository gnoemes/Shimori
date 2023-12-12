package com.gnoemes.shimori.sources.shikimori.models.anime

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CalendarResponse(
    @SerialName("anime") val anime : AnimeResponse,
    @SerialName("next_episode") val nextEpisode : Int,
    @SerialName("next_episode_at") val nextEpisodeDate: Instant?,
    @SerialName("duration") val duration : String
)