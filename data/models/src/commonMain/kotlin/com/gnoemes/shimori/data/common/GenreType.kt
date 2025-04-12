package com.gnoemes.shimori.data.common

enum class GenreType(val value: Int) {
    Genre(0), Tag(1),
    ;

    companion object {
        fun find(value: Int) = entries.find { it.value == value }
    }
}