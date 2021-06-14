package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.anime.Anime
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject


internal class CalendarMapper @Inject constructor(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<CalendarResponse, Anime> {
    override suspend fun map(from: CalendarResponse): Anime =
        animeResponseMapper.map(from.anime)
            .copy(
                    nextEpisode = from.nextEpisode,
                    nextEpisodeDate = from.nextEpisodeDate,
                    duration = convertDuration(from.nextEpisodeDate, from.duration)
            )

    private fun convertDuration(nextEpisodeDate: OffsetDateTime?, duration: String?): OffsetDateTime? {
        if (nextEpisodeDate == null || duration.isNullOrEmpty()) return null

        return when (duration.contains("/")) {
            true -> nextEpisodeDate.plusMinutes(duration.substring(0, duration.indexOf("/")).toDouble().toLong())
            else -> nextEpisodeDate.plusSeconds(duration.toDouble().toLong())
        }
    }
}