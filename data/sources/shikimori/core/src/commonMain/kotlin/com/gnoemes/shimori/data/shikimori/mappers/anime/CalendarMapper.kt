package com.gnoemes.shimori.data.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.shikimori.mappers.toInstant
import com.gnoemes.shimori.data.shikimori.models.anime.CalendarResponse
import com.gnoemes.shimori.data.titles.anime.Anime
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@Inject
class CalendarMapper constructor(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<CalendarResponse, Anime> {
    override fun map(from: CalendarResponse): Anime =
        animeResponseMapper.map(from.anime).copy(
            nextEpisode = from.nextEpisode,
            nextEpisodeDate = from.nextEpisodeDate?.toInstant(),
//            nextEpisodeEndDate = convertDuration(from.nextEpisodeDate, from.duration)
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