package com.gnoemes.shimori.common.utils

import android.content.Context
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.base.util.ShimoriDateTimeFormatter
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeType
import com.gnoemes.shimori.model.common.ContentStatus
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.qualifiers.ActivityContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

class ShimoriTextCreator @Inject constructor(
    @ActivityContext private val context: Context,
    private val prefs: ShimoriPreferences,
    private val formatter: ShimoriDateTimeFormatter
) {

    //TODO check locale
    fun name(anime: Anime): String {
        return if (prefs.isRomadziNaming) anime.name
        else anime.nameRu ?: anime.name
    }

    fun episodesDescription(anime: Anime): String? {
        return when (anime.status) {
            ContentStatus.ANONS -> {
                val date = anime.dateAired
                if (date != null) {
                    return formatter.formatMediumDate(date)
                }

                return context.getString(R.string.status_anons)
            }
            ContentStatus.ONGOING -> {
                val date = anime.nextEpisodeDate
                val episode = anime.nextEpisode

                if (date != null && episode != null) {
                    val now = OffsetDateTime.now()
                    if (date.isAfter(now)) {
                        val formatRes = R.string.ongoing_episode_format
                        val hours = ChronoUnit.HOURS.between(now, date)

                        if (hours < 1) {
                            val minutes = ChronoUnit.MINUTES.between(now, date)
                            return context.getString(formatRes, episode, minutes, context.getString(R.string.minute_short))
                        }

                        val days = ChronoUnit.DAYS.between(now, date)
                        if (days < 1) {
                            return context.getString(formatRes, episode, hours, context.getString(R.string.hour_short))
                        }

                        return context.getString(formatRes, episode, days, context.getString(R.string.day_short))
                    }
                }


                return context.getString(R.string.status_ongoing)
            }
            else -> null
        }
    }


    fun typeDescription(anime: Anime): String? {
        return when (anime.type) {
            AnimeType.TV -> context.getString(R.string.type_tv)
            AnimeType.OVA -> context.getString(R.string.type_ova)
            AnimeType.ONA -> context.getString(R.string.type_ona)
            AnimeType.MUSIC -> context.getString(R.string.type_music)
            AnimeType.MOVIE -> context.getString(R.string.type_movie)
            AnimeType.SPECIAL -> context.getString(R.string.type_special)
            else -> null
        }
    }

    fun listSortText(type: ListType, option: RateSortOption): String {
        val stringRes = when (option) {
            RateSortOption.PROGRESS -> R.string.list_sort_progress
            RateSortOption.DATE_CREATED -> R.string.list_sort_last_added
            RateSortOption.DATE_UPDATED -> R.string.list_sort_last_changed
            RateSortOption.DATE_AIRED -> R.string.list_sort_last_released
            RateSortOption.MY_SCORE -> R.string.list_sort_your_score
            RateSortOption.RATING -> R.string.list_sort_rating
            RateSortOption.NAME -> R.string.list_sort_name
            RateSortOption.SIZE -> {
                when (type) {
                    ListType.Anime -> R.string.list_sort_size_anime
                    else -> R.string.list_sort_size_manga
                }
            }
        }

        return context.getString(stringRes)
    }

    fun rateStatusText(type: RateTargetType, status: RateStatus): String {
        val stringRes = when (status) {
            RateStatus.WATCHING -> if (type.anime) R.string.List_page_watching else R.string.List_page_reading
            RateStatus.REWATCHING -> if (type.anime) R.string.List_page_re_watching else R.string.rate_status_manga_re_reading
            RateStatus.ON_HOLD -> R.string.List_page_on_hold
            RateStatus.PLANNED -> R.string.List_page_planned
            RateStatus.COMPLETED -> R.string.List_page_completed
            RateStatus.DROPPED -> R.string.List_page_dropped
        }

        return context.getString(stringRes)
    }
}