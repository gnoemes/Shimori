package com.gnoemes.shimori.data.core.entities.app

@kotlinx.serialization.Serializable
enum class Request(val tag : String) {
    CALENDAR("calendar"),
    ANIMES_WITH_STATUS("animes_with_status"),
    MANGAS_WITH_STATUS("mangas_with_status"),
    RANOBE_WITH_STATUS("ranobe_with_status"),
    SYNC_PENDING_RATES("sync_pending_rates"),
    ANIME_DETAILS("anime_details"),
    MANGA_DETAILS("manga_details"),
    RANOBE_DETAILS("ranobe_details"),
}