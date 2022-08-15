package com.gnoemes.shimori.common.ui.utils

//TODO use multiplatform resource generation?

interface ShimoriTextProvider {
    fun text(id: MessageID): String
}

@JvmInline
value class MessageID private constructor(val key: String) {
    companion object {
        val Today = MessageID("today")
        val AnonsDateFormat = MessageID("anons_date_format")
        val OngoingEpisodeFormat = MessageID("ongoing_episode_format")
        val MinuteShort = MessageID("minute_short")
        val HourShort = MessageID("hour_short")
        val DayShort = MessageID("day_short")
        val Anons = MessageID("status_anons")
        val Ongoing = MessageID("status_ongoing")
        val Released = MessageID("status_released")
        val TypeTV = MessageID("type_tv")
        val TypeMovie = MessageID("type_movie")
        val TypeSpecial = MessageID("type_special")
        val TypeMusic = MessageID("type_music")
        val TypeOva = MessageID("type_ova")
        val TypeOna = MessageID("type_ona")
        val TypeManga = MessageID("type_manga")
        val TypeManhua = MessageID("type_manhua")
        val TypeManhwa = MessageID("type_manhwa")
        val TypeOneShot = MessageID("type_one_shot")
        val TypeDoujin = MessageID("type_doujin")
        val TypeNovel = MessageID("type_novel")
        val TypeLightNovel = MessageID("type_light_novel")
        val SortProgress = MessageID("list_sort_progress")
        val SortEpisodes = MessageID("list_sort_size_anime")
        val SortChapters = MessageID("list_sort_size_manga")
        val SortLastChanged = MessageID("list_sort_last_changed")
        val SortLastAdded = MessageID("list_sort_last_added")
        val SortLastReleased = MessageID("list_sort_last_released")
        val SortName = MessageID("list_sort_name")
        val SortYourScore = MessageID("list_sort_your_score")
        val SortUsersScore = MessageID("list_sort_rating")
        val RateWatching = MessageID("list_page_watching")
        val RateReading = MessageID("list_page_reading")
        val RateReWatching = MessageID("list_page_re_watching")
        val RateReReading = MessageID("list_page_re_reading")
        val RateOnHold = MessageID("list_page_on_hold")
        val RatePlanned = MessageID("list_page_planned")
        val RateCompleted = MessageID("list_page_completed")
        val RateDropped = MessageID("list_page_dropped")
        val Undo = MessageID("undo")
        val TitlePinned = MessageID("notification_title_pinned")
        val TitleUnPinned = MessageID("notification_title_unpinned")
        val OAuthAccessDenied = MessageID("auth_revoke")
        val IncrementerHint = MessageID("inc_hint")
        val IncrementerFormat = MessageID("inc_added_progress")
    }
}

operator fun ShimoriTextProvider.get(id: MessageID): String = text(id)
operator fun ShimoriTextProvider.get(id: MessageID, vararg args: Any): String =
    text(id).format(args)