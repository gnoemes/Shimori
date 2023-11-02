package com.gnoemes.shimori.data.app

@kotlinx.serialization.Serializable
enum class Request(val tag : String) {
    CALENDAR("calendar"),
    ANIMES_WITH_STATUS("animes_with_status"),
    MANGAS_WITH_STATUS("mangas_with_status"),
    RANOBE_WITH_STATUS("ranobe_with_status"),
    SYNC_PENDING_TRACKS("sync_pending_tracks"),
    ANIME_DETAILS("anime_details"),
    MANGA_DETAILS("manga_details"),
    RANOBE_DETAILS("ranobe_details"),
    CHARACTER_DETAILS("character_details"),
    ANIME_ROLES("anime_roles"),
    MANGA_ROLES("manga_roles"),
    RANOBE_ROLES("ranobe_roles"),
}