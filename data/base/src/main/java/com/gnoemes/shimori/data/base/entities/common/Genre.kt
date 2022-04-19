package com.gnoemes.shimori.data.base.entities.common

@kotlinx.serialization.Serializable
enum class Genre(val shikimoriValue: String) {
    ACTION("action"),
    ADVENTURE("adventure"),
    CARS("cars"),
    COMEDY("comedy"),
    DEMENTIA("dementia"),
    DEMONS("demons"),
    MYSTERY("mystery"),
    DRAMA("drama"),
    ECCHI("ecchi"),
    FANTASY("fantasy"),
    GAME("game"),
    HENTAI("hentai"),
    HISTORICAL("historical"),
    HORROR("horror"),
    KIDS("kids"),
    MAGIC("magic"),
    MARTIAL_ARTS("martial_arts"),
    MECHA("mecha"),
    MUSIC("music"),
    PARODY("parody"),
    SAMURAI("samurai"),
    ROMANCE("romance"),
    SCHOOL("school"),
    SCI_FI("sci_fi"),
    SHOUJO("shoujo"),
    SHOUJO_AI("shoujo_ai"),
    SHOUNEN("shounen"),
    SHOUNEN_AI("shounen_ai"),
    SPACE("space"),
    SPORTS("sports"),
    SUPER_POWER("super_power"),
    VAMPIRE("vampire"),
    YAOI("yaoi"),
    YURI("yuri"),
    HAREM("harem"),
    SLICE_OF_LIFE("slice_of_life"),
    SUPERNATURAL("supernatural"),
    MILITATY("military"),
    POLICE("police"),
    PSYCHOLOGICAL("psychological"),
    THRILLER("thriller"),
    SEINEN("seinen"),
    JOSEI("josei"),
    GENDER_BENDER("gender_bender"),
    DOUJINSHI("doujinshi");

    companion object {
        private val values by lazy { values() }
        fun fromShikimori(value: String) = values.firstOrNull { it.shikimoriValue == value }
    }
}