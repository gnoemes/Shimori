package com.gnoemes.shimori.source

interface Source {
    /**
     * Must be unique
     */
    val id: Long

    val name: String

    val lang: String get() = ""
}