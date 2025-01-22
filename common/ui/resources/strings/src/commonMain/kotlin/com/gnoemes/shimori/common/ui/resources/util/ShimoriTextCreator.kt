package com.gnoemes.shimori.common.ui.resources.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.gnoemes.shimori.common.ui.resources.strings.anime
import com.gnoemes.shimori.common.ui.resources.strings.anons_date_format
import com.gnoemes.shimori.common.ui.resources.strings.day_short
import com.gnoemes.shimori.common.ui.resources.strings.hour_short
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_last_added
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_last_changed
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_last_released
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_name
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_progress
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_rating
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_size_anime
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_size_manga
import com.gnoemes.shimori.common.ui.resources.strings.list_sort_your_score
import com.gnoemes.shimori.common.ui.resources.strings.manga
import com.gnoemes.shimori.common.ui.resources.strings.minute_short
import com.gnoemes.shimori.common.ui.resources.strings.ongoing_episode_format
import com.gnoemes.shimori.common.ui.resources.strings.ranobe
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_anime_re_watching
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_anime_watching
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_completed
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_dropped
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_manga_re_reading
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_manga_reading
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_on_hold
import com.gnoemes.shimori.common.ui.resources.strings.rate_status_planned
import com.gnoemes.shimori.common.ui.resources.strings.settings_language_english
import com.gnoemes.shimori.common.ui.resources.strings.settings_language_romaji
import com.gnoemes.shimori.common.ui.resources.strings.settings_language_russian
import com.gnoemes.shimori.common.ui.resources.strings.settings_theme_dark
import com.gnoemes.shimori.common.ui.resources.strings.settings_theme_light
import com.gnoemes.shimori.common.ui.resources.strings.settings_theme_system
import com.gnoemes.shimori.common.ui.resources.strings.status_anons
import com.gnoemes.shimori.common.ui.resources.strings.status_ongoing
import com.gnoemes.shimori.common.ui.resources.strings.today
import com.gnoemes.shimori.common.ui.resources.strings.type_doujin
import com.gnoemes.shimori.common.ui.resources.strings.type_light_novel
import com.gnoemes.shimori.common.ui.resources.strings.type_manga
import com.gnoemes.shimori.common.ui.resources.strings.type_manhua
import com.gnoemes.shimori.common.ui.resources.strings.type_manhwa
import com.gnoemes.shimori.common.ui.resources.strings.type_movie
import com.gnoemes.shimori.common.ui.resources.strings.type_music
import com.gnoemes.shimori.common.ui.resources.strings.type_novel
import com.gnoemes.shimori.common.ui.resources.strings.type_ona
import com.gnoemes.shimori.common.ui.resources.strings.type_one_shot
import com.gnoemes.shimori.common.ui.resources.strings.type_ova
import com.gnoemes.shimori.common.ui.resources.strings.type_special
import com.gnoemes.shimori.common.ui.resources.strings.type_tv
import com.gnoemes.shimori.data.ShimoriContentEntity
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeType
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaType
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.AppTitlesLocale
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import org.jetbrains.compose.resources.stringResource

class ShimoriTextCreator(
    private val formatter: ShimoriDateTextFormatter,
    private val locale: AppLocale,
    private val titlesLocale: AppTitlesLocale,
) {
    val divider by lazy { "•" }

    @Composable
    inline fun build(crossinline block: @Composable ShimoriTextCreator.() -> Unit) =
        block()

    @Composable
    inline operator fun <T : CharSequence> invoke(block: @Composable ShimoriTextCreator.() -> T): T {
        return this.block()
    }


    @Composable
    private fun String.colorSpan(
        color: Color = MaterialTheme.colorScheme.tertiary
    ) = AnnotatedString(
        this,
        spanStyle = SpanStyle(
            color = color
        )
    )

    @Composable
    fun AppLocale.name(): String {
        return when (this) {
            AppLocale.Russian -> stringResource(Strings.settings_language_russian)
            else -> stringResource(Strings.settings_language_english)
        }
    }

    @Composable
    fun AppTitlesLocale.name(): String {
        return when (this) {
            AppTitlesLocale.Russian -> stringResource(Strings.settings_language_russian)
            AppTitlesLocale.Romaji -> stringResource(Strings.settings_language_romaji)
            else -> stringResource(Strings.settings_language_english)
        }
    }

    @Composable
    fun AppTheme.name(): String {
        return when (this) {
            AppTheme.SYSTEM -> stringResource(Strings.settings_theme_system)
            AppTheme.DARK -> stringResource(Strings.settings_theme_dark)
            else -> stringResource(Strings.settings_theme_light)
        }
    }

    @Composable
    fun TrackTargetType.name(): String {
        return when (this) {
            TrackTargetType.ANIME -> stringResource(Strings.anime)
            TrackTargetType.MANGA -> stringResource(Strings.manga)
            TrackTargetType.RANOBE -> stringResource(Strings.ranobe)
        }
    }

    @Composable
    fun TrackStatus.name(type: TrackTargetType): String = when (this) {
        TrackStatus.WATCHING -> if (type.anime) stringResource(Strings.rate_status_anime_watching)
        else stringResource(Strings.rate_status_manga_reading)

        TrackStatus.REWATCHING -> if (type.anime) stringResource(Strings.rate_status_anime_re_watching)
        else stringResource(Strings.rate_status_manga_re_reading)

        TrackStatus.ON_HOLD -> stringResource(Strings.rate_status_on_hold)
        TrackStatus.PLANNED -> stringResource(Strings.rate_status_planned)
        TrackStatus.COMPLETED -> stringResource(Strings.rate_status_completed)
        TrackStatus.DROPPED -> stringResource(Strings.rate_status_dropped)
    }

    @Composable
    fun ListSortOption.name(type: TrackTargetType): String = when (this) {
        ListSortOption.PROGRESS -> stringResource(Strings.list_sort_progress)
        ListSortOption.DATE_CREATED -> stringResource(Strings.list_sort_last_added)
        ListSortOption.DATE_UPDATED -> stringResource(Strings.list_sort_last_changed)
        ListSortOption.DATE_AIRED -> stringResource(Strings.list_sort_last_released)
        ListSortOption.MY_SCORE -> stringResource(Strings.list_sort_your_score)
        ListSortOption.RATING -> stringResource(Strings.list_sort_rating)
        ListSortOption.NAME -> stringResource(Strings.list_sort_name)
        ListSortOption.SIZE -> {
            when (type) {
                TrackTargetType.ANIME -> stringResource(Strings.list_sort_size_anime)
                else -> stringResource(Strings.list_sort_size_manga)
            }
        }
    }

    fun ShimoriContentEntity.name(): String {
        return when (titlesLocale) {
            AppTitlesLocale.English -> this.nameEn ?: defaultNameForLocale
            AppTitlesLocale.Russian -> this.nameRu ?: defaultNameForLocale
            else -> this.name
        }
    }

    private val ShimoriContentEntity.defaultNameForLocale
        get() = when (locale) {
            AppLocale.English -> this.nameEn ?: this.name
            AppLocale.Russian -> this.nameRu ?: this.name
            else -> this.name
        }

    @Composable
    fun Anime.status(): AnnotatedString? {
        return when (status) {
            TitleStatus.ANONS -> {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val date = dateAired
                if (date != null && date > now.date) {
                    val days = now.date.daysUntil(date)

                    if (days >= 30) {
                        return formatter.formatMediumDate(date).colorSpan()
                    }

                    if (days == 0) {
                        return stringResource(Strings.today).colorSpan()
                    }

                    return stringResource(
                        Strings.anons_date_format,
                        days,
                        stringResource(Strings.day_short)
                    ).colorSpan()
                }

                return stringResource(Strings.status_anons).colorSpan()
            }

            TitleStatus.ONGOING -> {
                val date = nextEpisodeDate
                val episode = nextEpisode

                if (date != null && episode != null) {
                    val now = Clock.System.now()
                    if (date > now) {
                        val format = stringResource(Strings.ongoing_episode_format)
                        val hours = now.until(date, DateTimeUnit.HOUR)

                        if (hours < 1) {
                            val minutes = now.until(date, DateTimeUnit.MINUTE)
                            return format.format(
                                episode,
                                minutes,
                                stringResource(Strings.minute_short)
                            ).colorSpan()
                        }

                        val days = now.daysUntil(date, TimeZone.currentSystemDefault())
                        if (days < 1) {
                            return format.format(
                                episode,
                                hours,
                                stringResource(Strings.hour_short)
                            ).colorSpan()
                        }

                        return format.format(
                            episode,
                            days,
                            stringResource(Strings.day_short)
                        ).colorSpan()
                    }
                }

                return stringResource(Strings.status_ongoing).colorSpan()
            }

            TitleStatus.RELEASED -> dateReleased?.year?.toString()?.colorSpan()
            else -> null
        }
    }

    @Composable
    fun Anime.type(): String? {
        return when (animeType) {
            AnimeType.Tv -> stringResource(Strings.type_tv)
            AnimeType.OVA -> stringResource(Strings.type_ova)
            AnimeType.ONA -> stringResource(Strings.type_ona)
            AnimeType.Music -> stringResource(Strings.type_music)
            AnimeType.Movie -> stringResource(Strings.type_movie)
            AnimeType.Special -> stringResource(Strings.type_special)
            else -> null
        }
    }


    @Composable
    fun Manga.status(): AnnotatedString? {
        return when (status) {
            TitleStatus.ANONS -> stringResource(Strings.status_anons).colorSpan()
            TitleStatus.ONGOING -> stringResource(Strings.status_ongoing).colorSpan()
            TitleStatus.RELEASED -> dateReleased?.year?.toString()?.colorSpan()
            else -> null
        }
    }

    @Composable
    fun Manga.type(): String? {
        return when (mangaType) {
            MangaType.Manga -> stringResource(Strings.type_manga)
            MangaType.Manhua -> stringResource(Strings.type_manhua)
            MangaType.Manhwa -> stringResource(Strings.type_manhwa)
            MangaType.OneShot -> stringResource(Strings.type_one_shot)
            MangaType.Doujin -> stringResource(Strings.type_doujin)
            else -> null
        }
    }


    @Composable
    fun Ranobe.status(): AnnotatedString? {
        return when (status) {
            TitleStatus.ANONS -> stringResource(Strings.status_anons).colorSpan()
            TitleStatus.ONGOING -> stringResource(Strings.status_ongoing).colorSpan()
            TitleStatus.RELEASED -> dateReleased?.year?.toString()?.colorSpan()
            else -> null
        }
    }

    @Composable
    fun Ranobe.type(): String? {
        return when (ranobeType) {
            RanobeType.Novel -> stringResource(Strings.type_novel)
            RanobeType.LightNovel -> stringResource(Strings.type_light_novel)
            else -> null
        }
    }

    @Composable
    fun TitleWithTrackEntity.progress(): String {
        val progress = "${track?.progress ?: 0}"
        val size = "${entity.size ?: "?"}"
        return "$progress / $size"
    }

//
//    fun scoreDescription(score: Double?): String? {
//        if (score == null || score == 0.0) return null
//        //TODO score format
//        return score.toString()
//    }
//
//
//    fun releaseDate(title: ShimoriTitleEntity): String? {
//        if (title.status == TitleStatus.DISCONTINUED) {
//            return textProvider[MessageID.Discontinued]
//        }
//
//        val date = title.dateReleased ?: return null
//        return buildString {
//            append(formatter.formatMediumDate(date))
//
//            if (title.isOngoing) {
//                append("-")
//            }
//        }
//    }
//
//    fun duration(anime: Anime): String? {
//        val duration = anime.duration ?: return null
//
//        val format = textProvider[MessageID.EpisodeDurationFormat]
//
//        val hourText = textProvider[MessageID.HourShort]
//        val minuteText = textProvider[MessageID.MinuteShort]
//
//        return if (duration == 60) {
//            format.format(duration / 60, hourText)
//        } else if (duration > 60) {
//            format.format(duration, "${duration / 60} $hourText ${duration % 60} $minuteText")
//        } else {
//            format.format(duration, minuteText)
//        }
//    }
//
//    fun ageRating(ageRating: AgeRating): String? = when (ageRating) {
//        AgeRating.RX -> "Rx"
//        AgeRating.R_PLUS -> "R+"
//        AgeRating.R -> "R-17"
//        AgeRating.PG_13 -> "PG-13"
//        AgeRating.PG -> "PG"
//        AgeRating.G -> "G"
//        else -> null
//    }
//
//    fun genre(genre: Genre): String {
//        return when (titlesLocale) {
//            AppTitlesLocale.English -> genre.name
//            AppTitlesLocale.Russian -> genre.nameRu ?: genre.name
//            else -> genre.name
//        }
//    }
}
