package com.gnoemes.shikimori.entities.anime


@kotlinx.serialization.Serializable
internal sealed class ShikimoriAnimeVideoType(internal val raw: String) {
    object Opening : ShikimoriAnimeVideoType("op")
    object Ending : ShikimoriAnimeVideoType("ed")
    object Promo : ShikimoriAnimeVideoType("pv")
    object Commercial : ShikimoriAnimeVideoType("cm")
    object Other : ShikimoriAnimeVideoType("other")
    companion object {
        fun find(type: String?): ShikimoriAnimeVideoType {
            return when (type) {
                Opening.raw -> Opening
                Ending.raw -> Ending
                Promo.raw -> Promo
                Commercial.raw -> Promo
                else -> Other
            }
        }
    }
}