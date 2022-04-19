package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.mappers.Mapper
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.plus


internal class CalendarMapper constructor(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<CalendarResponse, Anime> {
    override suspend fun map(from: CalendarResponse): Anime =
        animeResponseMapper.map(from.anime).copy(
            nextEpisode = from.nextEpisode,
            nextEpisodeDate = from.nextEpisodeDate,
            duration = convertDuration(from.nextEpisodeDate, from.duration)
        )

    private fun convertDuration(
        nextEpisodeDate: DateTimePeriod?, duration: String?
    ): DateTimePeriod? {
        if (nextEpisodeDate == null || duration.isNullOrEmpty()) return null

        return when (duration.contains("/")) {
            true -> nextEpisodeDate + DateTimePeriod(
                minutes = duration.substring(
                    0, duration.indexOf("/")
                ).toDouble().toInt()
            )
            else -> nextEpisodeDate + DateTimePeriod(
                seconds = duration.toDouble().toInt()
            )
        }
    }
}