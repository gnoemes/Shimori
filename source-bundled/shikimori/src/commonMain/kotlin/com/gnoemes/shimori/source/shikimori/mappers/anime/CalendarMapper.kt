package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.shikimori.mappers.toInstant
import com.gnoemes.shimori.source.shikimori.models.anime.CalendarResponse
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@Inject
class CalendarMapper(
    private val animeResponseMapper: AnimeResponseMapper
) : Mapper<CalendarResponse, SAnime> {
    override fun map(from: CalendarResponse): SAnime =
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