package com.gnoemes.shimori.data.app

@kotlinx.serialization.Serializable
enum class Request(val tag : String) {
    CALENDAR("calendar"),
    ANIMES_WITH_STATUS("animes_with_status"),
    MANGAS_WITH_STATUS("mangas_with_status"),
    RANOBE_WITH_STATUS("ranobe_with_status"),
    SYNC_PENDING_TRACKS("sync_pending_tracks"),
    ANIME_DETAILS("anime_details"),
    ANIME_DETAILS_CHARACTERS("anime_details_characters"),
    ANIME_DETAILS_PERSONS("anime_details_persons"),
    ANIME_DETAILS_RELATED("anime_details_related"),
    MANGA_DETAILS("manga_details"),
    MANGA_DETAILS_CHARACTERS("manga_details_characters"),
    MANGA_DETAILS_PERSONS("manga_details_persons"),
    MANGA_DETAILS_RELATED("manga_details_related"),
    RANOBE_DETAILS("ranobe_details"),
    RANOBE_DETAILS_CHARACTERS("ranobe_details_characters"),
    RANOBE_DETAILS_PERSONS("ranobe_details_persons"),
    RANOBE_DETAILS_RELATED("ranobe_details_related"),
    CHARACTER_DETAILS("character_details"),
    PERSON_DETAILS("person_details"),

    GENRES("genres"),
    STUDIOS("studios"),
}