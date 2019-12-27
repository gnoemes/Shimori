package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.anime.Anime
import org.joda.time.DateTime
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

    private fun convertDuration(nextEpisodeDate: DateTime, duration: String): DateTime? {
        return when (duration.contains("/")) {
            true -> DateTime(nextEpisodeDate).plusMinutes(duration.substring(0, duration.indexOf("/")).toDouble().toInt())
            else -> DateTime(nextEpisodeDate).plusSeconds(duration.toDouble().toInt())
        }
    }
}