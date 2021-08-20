package com.gnoemes.shimori.model.manga

@JvmInline
value class MangaType(val type: String) {
    companion object {
        val Manga = MangaType("manga")
        val Manhwa = MangaType("manhwa")
        val Manhua = MangaType("manhua")
        val OneShot = MangaType("one_shot")
        val Doujin = MangaType("doujin")


        fun find(type: String?) = when (type) {
            "manga" -> Manga
            "manhwa" -> Manhwa
            "manhua" -> Manhua
            "one_shot" -> OneShot
            "doujin" -> Doujin
            else -> null
        }
    }
}
