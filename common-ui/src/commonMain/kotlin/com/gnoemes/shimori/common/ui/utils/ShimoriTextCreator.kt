package com.gnoemes.shimori.common.ui.utils

import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.data.core.entities.ShimoriContentEntity
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeType
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaType
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import kotlinx.datetime.*

class ShimoriTextCreator(
    private val textProvider: ShimoriTextProvider,
    private val formatter: ShimoriDateTimeFormatter,
    private val titlesLocale: AppTitlesLocale,
    private val appLocale: AppLocale
) {

    fun name(title: ShimoriContentEntity): String {
        return when (titlesLocale) {
            AppTitlesLocale.English -> title.nameEn ?: defaultNameForLocale(title)
            AppTitlesLocale.Russian -> title.nameRu ?: defaultNameForLocale(title)
            else -> title.name
        }
    }

    private fun defaultNameForLocale(title: ShimoriContentEntity) = when (appLocale) {
        AppLocale.English -> title.nameEn ?: title.name
        AppLocale.Russian -> title.nameRu ?: title.name
        else -> title.name
    }

    fun statusDescription(anime: Anime): String? {
        return when (anime.status) {
            TitleStatus.ANONS -> {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val date = anime.dateAired
                if (date != null && date > now) {
                    val days = now.daysUntil(date)

                    if (days >= 30) {
                        return formatter.formatMediumDate(date)
                    }

                    if (days == 0) {
                        return textProvider[MessageID.Today]
                    }

                    val daysShort = textProvider[MessageID.DayShort]
                    return textProvider[MessageID.AnonsDateFormat].format(days, daysShort)
                }

                return textProvider[MessageID.Anons]
            }
            TitleStatus.ONGOING -> {
                val date = anime.nextEpisodeDate
                val episode = anime.nextEpisode

                if (date != null && episode != null) {
                    val now = Clock.System.now()
                    if (date > now) {
                        val format = textProvider[MessageID.OngoingEpisodeFormat]
                        val hours = now.until(date, DateTimeUnit.HOUR)

                        if (hours < 1) {
                            val minutes = now.until(date, DateTimeUnit.MINUTE)
                            return format.format(
                                episode, minutes, textProvider[MessageID.MinuteShort]
                            )
                        }

                        val days = now.daysUntil(date, TimeZone.currentSystemDefault())
                        if (days < 1) {
                            return format.format(
                                episode, hours, textProvider[MessageID.HourShort]
                            )
                        }

                        return format.format(
                            episode, days, textProvider[MessageID.DayShort]
                        )
                    }
                }

                return textProvider[MessageID.Ongoing]
            }
            TitleStatus.RELEASED -> anime.dateReleased?.year?.toString()
            else -> null
        }
    }

    fun statusDescription(manga: Manga): String? {
        return when (manga.status) {
            TitleStatus.ANONS -> textProvider[MessageID.Anons]
            TitleStatus.ONGOING -> textProvider[MessageID.Ongoing]
            TitleStatus.RELEASED -> manga.dateReleased?.year?.toString()
            else -> null
        }
    }

    fun statusDescription(ranobe: Ranobe): String? {
        return when (ranobe.status) {
            TitleStatus.ANONS -> textProvider[MessageID.Anons]
            TitleStatus.ONGOING -> textProvider[MessageID.Ongoing]
            TitleStatus.RELEASED -> ranobe.dateReleased?.year?.toString()
            else -> null
        }
    }

    fun typeDescription(anime: Anime): String? {
        return when (anime.animeType) {
            AnimeType.Tv -> textProvider[MessageID.TypeTV]
            AnimeType.OVA -> textProvider[MessageID.TypeOva]
            AnimeType.ONA -> textProvider[MessageID.TypeOna]
            AnimeType.Music -> textProvider[MessageID.TypeMusic]
            AnimeType.Movie -> textProvider[MessageID.TypeMovie]
            AnimeType.Special -> textProvider[MessageID.TypeSpecial]
            else -> null
        }
    }

    fun typeDescription(manga: Manga): String? {
        return when (manga.mangaType) {
            MangaType.Manga -> textProvider[MessageID.TypeManga]
            MangaType.Manhua -> textProvider[MessageID.TypeManhua]
            MangaType.Manhwa -> textProvider[MessageID.TypeManhwa]
            MangaType.OneShot -> textProvider[MessageID.TypeOneShot]
            MangaType.Doujin -> textProvider[MessageID.TypeDoujin]
            else -> null
        }
    }

    fun typeDescription(ranobe: Ranobe): String? {
        return when (ranobe.ranobeType) {
            RanobeType.Novel -> textProvider[MessageID.TypeNovel]
            RanobeType.LightNovel -> textProvider[MessageID.TypeLightNovel]
            else -> null
        }
    }

    fun scoreDescription(score: Double?): String? {
        if (score == null || score == 0.0) return null
        //TODO score format
        return score.toString()
    }

    fun listSortText(type: ListType, option: ListSortOption): String = when (option) {
        ListSortOption.PROGRESS -> textProvider[MessageID.SortProgress]
        ListSortOption.DATE_CREATED -> textProvider[MessageID.SortLastAdded]
        ListSortOption.DATE_UPDATED -> textProvider[MessageID.SortLastChanged]
        ListSortOption.DATE_AIRED -> textProvider[MessageID.SortLastReleased]
        ListSortOption.MY_SCORE -> textProvider[MessageID.SortYourScore]
        ListSortOption.RATING -> textProvider[MessageID.SortUsersScore]
        ListSortOption.NAME -> textProvider[MessageID.SortName]
        ListSortOption.SIZE -> {
            when (type) {
                ListType.Anime -> textProvider[MessageID.SortEpisodes]
                else -> textProvider[MessageID.SortChapters]
            }
        }
    }

    fun trackStatusText(type: TrackTargetType, status: TrackStatus): String = when (status) {
        TrackStatus.WATCHING -> if (type.anime) textProvider[MessageID.TrackWatching]
        else textProvider[MessageID.TrackReading]
        TrackStatus.REWATCHING -> if (type.anime) textProvider[MessageID.TrackReWatching]
        else textProvider[MessageID.TrackReReading]
        TrackStatus.ON_HOLD -> textProvider[MessageID.TrackOnHold]
        TrackStatus.PLANNED -> textProvider[MessageID.TrackPlanned]
        TrackStatus.COMPLETED -> textProvider[MessageID.TrackCompleted]
        TrackStatus.DROPPED -> textProvider[MessageID.TrackDropped]
    }

    fun releaseDate(title: ShimoriTitleEntity): String? {
        if (title.status == TitleStatus.DISCONTINUED) {
            return textProvider[MessageID.Discontinued]
        }

        val date = title.dateReleased ?: return null
        return buildString {
            append(formatter.formatMediumDate(date))

            if (title.isOngoing) {
                append("-")
            }
        }
    }

    fun duration(anime: Anime): String? {
        val duration = anime.duration ?: return null

        val format = textProvider[MessageID.EpisodeDurationFormat]

        val hourText = textProvider[MessageID.HourShort]
        val minuteText = textProvider[MessageID.MinuteShort]

        return if (duration == 60) {
            format.format(duration / 60, hourText)
        } else if (duration > 60) {
            format.format(duration, "${duration / 60} $hourText ${duration % 60} $minuteText")
        } else {
            format.format(duration, minuteText)
        }
    }

    fun ageRating(ageRating: AgeRating): String? = when (ageRating) {
        AgeRating.RX -> "Rx"
        AgeRating.R_PLUS -> "R+"
        AgeRating.R -> "R-17"
        AgeRating.PG_13 -> "PG-13"
        AgeRating.PG -> "PG"
        AgeRating.G -> "G"
        else -> null
    }
}