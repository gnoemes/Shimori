package com.gnoemes.shimori.data.base.entities.titles.ranobe

@JvmInline
@kotlinx.serialization.Serializable
value class RanobeType(val type: String) {
    companion object {
        val Novel = RanobeType("novel")
        val LightNovel = RanobeType("light_novel")

        fun find(type: String?) = when (type) {
            "novel" -> Novel
            "light_novel" -> LightNovel
            else -> null
        }
    }
}