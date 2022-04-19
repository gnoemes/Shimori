package com.gnoemes.shikimori.entities.anime

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class CalendarResponse(
    @SerialName("anime") val anime : AnimeResponse,
    @SerialName("next_episode") val nextEpisode : Int,
    @SerialName("next_episode_at") val nextEpisodeDate: DateTimePeriod?,
    @SerialName("duration") val duration : String
)