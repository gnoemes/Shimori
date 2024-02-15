package com.gnoemes.shimori.data.app

@JvmInline
@kotlinx.serialization.Serializable
value class SourceDataType private constructor(val type: Int) {

    companion object {
        val Track = SourceDataType(1)
        val Anime = SourceDataType(2)
        val Manga = SourceDataType(3)
        val Ranobe = SourceDataType(4)
        val Character = SourceDataType(5)
        val Person = SourceDataType(6)
        val User = SourceDataType(7)
    }

    fun find(type : Int) = when(type) {
        1 -> Track
        2 -> Anime
        3 -> Manga
        4 -> Ranobe
        5 -> Character
        6 -> Person
        7 -> User
        else -> null
    }

    override fun toString(): String {
        return when(type){
            1 -> "Track"
            2 -> "Anime"
            3 -> "Manga"
            4 -> "Ranobe"
            5 -> "Character"
            6 -> "Person"
            7 -> "User"
            else -> "Unknown type"
        }
    }
}