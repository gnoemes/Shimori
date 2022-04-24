package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.mappers.Mapper
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


internal class CalendarMapper constructor(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<CalendarResponse, Anime> {
    override suspend fun map(from: CalendarResponse): Anime =
        animeResponseMapper.map(from.anime).copy(
            nextEpisode = from.nextEpisode,
            nextEpisodeDate = from.nextEpisodeDate,
            nextEpisodeEndDate = convertDuration(from.nextEpisodeDate, from.duration)
        )

    private fun convertDuration(
        nextEpisodeDate: Instant?, duration: String?
    ): Instant? {
        if (nextEpisodeDate == null || duration.isNullOrEmpty()) return null

        return when (duration.contains("/")) {
            true -> nextEpisodeDate.plus(
                duration.substring(0, duration.indexOf("/")).toDouble().toInt().minutes
            )
            else -> nextEpisodeDate.plus(duration.toDouble().toInt().seconds)
        }
    }
}