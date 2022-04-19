package com.gnoemes.shimori.data.base.entities.app

@kotlinx.serialization.Serializable
enum class Request(val tag : String) {
    CALENDAR("calendar"),
    ANIMES_WITH_STATUS("animes_with_status"),
    MANGAS_WITH_STATUS("mangas_with_status"),
    RANOBE_WITH_STATUS("ranobe_with_status"),
}