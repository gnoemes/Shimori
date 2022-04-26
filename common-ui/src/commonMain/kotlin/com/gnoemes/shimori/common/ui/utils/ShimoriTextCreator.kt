package com.gnoemes.shimori.common.ui.utils

import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.data.base.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.base.entities.common.TitleStatus
import com.gnoemes.shimori.data.base.entities.rate.ListType
import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.entities.titles.anime.AnimeType
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.entities.titles.manga.MangaType
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.entities.titles.ranobe.RanobeType
import kotlinx.datetime.*

class ShimoriTextCreator(
    private val textProvider: ShimoriTextProvider,
    private val formatter: ShimoriDateTimeFormatter,
    private val titlesLocale: AppTitlesLocale,
    private val appLocale: AppLocale
) {

    fun name(title: ShimoriTitleEntity): String {
        return when (titlesLocale) {
            AppTitlesLocale.English -> title.nameEn ?: defaultForLocale(title)
            AppTitlesLocale.Russian -> title.nameRu ?: defaultForLocale(title)
            else -> title.name
        }
    }

    private fun defaultForLocale(title: ShimoriTitleEntity) =
        when (appLocale) {
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

                    val format = textProvider[MessageID.AnonsDateFormat]
                    val daysShort = textProvider[MessageID.DayShort]
                    return format.format(days, daysShort)
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
                                episode,
                                minutes,
                                textProvider[MessageID.MinuteShort]
                            )
                        }

                        val days = now.daysUntil(date, TimeZone.currentSystemDefault())
                        if (days < 1) {
                            return format.format(
                                episode,
                                hours,
                                textProvider[MessageID.HourShort]
                            )
                        }

                        return format.format(
                            episode,
                            days,
                            textProvider[MessageID.DayShort]
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
        //TODO score format
        return score?.toString()
    }

    fun listSortText(type: ListType, option: RateSortOption): String = when (option) {
        RateSortOption.PROGRESS -> textProvider[MessageID.SortProgress]
        RateSortOption.DATE_CREATED -> textProvider[MessageID.SortLastAdded]
        RateSortOption.DATE_UPDATED -> textProvider[MessageID.SortLastChanged]
        RateSortOption.DATE_AIRED -> textProvider[MessageID.SortLastReleased]
        RateSortOption.MY_SCORE -> textProvider[MessageID.SortYourScore]
        RateSortOption.RATING -> textProvider[MessageID.SortUsersScore]
        RateSortOption.NAME -> textProvider[MessageID.SortName]
        RateSortOption.SIZE -> {
            when (type) {
                ListType.Anime -> textProvider[MessageID.SortEpisodes]
                else -> textProvider[MessageID.SortChapters]
            }
        }
    }

    fun rateStatusText(type: RateTargetType, status: RateStatus): String = when (status) {
        RateStatus.WATCHING ->
            if (type.anime) textProvider[MessageID.RateWatching]
            else textProvider[MessageID.RateReading]
        RateStatus.REWATCHING ->
            if (type.anime) textProvider[MessageID.RateReWatching]
            else textProvider[MessageID.RateReReading]
        RateStatus.ON_HOLD -> textProvider[MessageID.RateOnHold]
        RateStatus.PLANNED -> textProvider[MessageID.RatePlanned]
        RateStatus.COMPLETED -> textProvider[MessageID.RateCompleted]
        RateStatus.DROPPED -> textProvider[MessageID.RateDropped]
    }
}